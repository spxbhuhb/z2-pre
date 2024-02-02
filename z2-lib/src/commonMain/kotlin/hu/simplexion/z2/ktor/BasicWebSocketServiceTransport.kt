package hu.simplexion.z2.ktor

import hu.simplexion.z2.serialization.protobuf.ProtoDecoder
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.util.Lock
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.use
import hu.simplexion.z2.util.vmNowMicro
import hu.simplexion.z2.services.transport.ServiceErrorHandler
import hu.simplexion.z2.services.transport.ServiceResultException
import hu.simplexion.z2.services.transport.ServiceTimeoutException
import hu.simplexion.z2.services.transport.RequestEnvelope
import hu.simplexion.z2.services.transport.ResponseEnvelope
import hu.simplexion.z2.services.transport.ServiceCallStatus
import hu.simplexion.z2.services.transport.ServiceCallTransport
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.collections.set

open class BasicWebSocketServiceTransport(
    val path: String = "/z2/service",
    var errorHandler: ServiceErrorHandler? = null,
    val trace: Boolean = false,
) : ServiceCallTransport {

    val callTimeout = 20_000_000

    class OutgoingCall(
        val request: RequestEnvelope,
        val createdMicros: Long = vmNowMicro(),
        val responseChannel: Channel<ResponseEnvelope> = Channel(1)
    )

    var retryDelay = 200L // milliseconds
    val scope = CoroutineScope(Dispatchers.Default)

    val outgoingLock = Lock()
    private val outgoingCalls = Channel<OutgoingCall>(Channel.UNLIMITED)
    val pendingCalls = mutableMapOf<UUID<RequestEnvelope>, OutgoingCall>()

    val client = HttpClient {
        install(WebSockets) {
            pingInterval = 20_000
        }
    }

    fun start() {
        scope.launch { run() }
        scope.launch { timeout() }
    }

    suspend fun run() {
        while (scope.isActive) {
            try {
                retryDelay = 200 // reset the retry delay as we have a working connection

                client.webSocket(path) {

                    launch {
                        try {
                            for (call in outgoingCalls) {
                                try {
                                    send(Frame.Binary(true, RequestEnvelope.encodeProto(call.request)))
                                    pendingCalls[call.request.callId] = call
                                } catch (ex: CancellationException) {
                                    postponeAfterCancel(call)
                                    // break to get out of for, so we can retry
                                    break
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    for (frame in incoming) {

                        frame as? Frame.Binary ?: continue

                        val responseEnvelope = ResponseEnvelope.decodeProto(ProtoMessage(frame.data))

                        val call = pendingCalls.remove(responseEnvelope.callId)
                        if (call != null) {
                            call.responseChannel.trySend(responseEnvelope)
                        } else {
                            errorHandler?.callError("", "", responseEnvelope)
                        }
                    }
                }

            } catch (ex: Exception) {
                connectionError(ex)
                delay(retryDelay) // wait a bit before trying to re-establish the connection
                if (retryDelay < 5_000) retryDelay = (retryDelay * 115) / 100
            }
        }
    }

    private fun postponeAfterCancel(call: OutgoingCall) {
        outgoingLock.use {

            val cutoff = vmNowMicro() - callTimeout

            // if we get a cancellation exception we try to reopen the connection and retry the calls
            // however the call that failed is removed from [outgoingCalls], so we copy remove all
            // entries from [outgoingCalls] and put them in again, here [outgoingCalls] is protected by
            // [outgoingLock], so this copy should be fine even if someone calls [call] during the copy

            val calls = mutableListOf<OutgoingCall>()

            var next: OutgoingCall? = call

            while (next != null) {
                if (next.createdMicros < cutoff) {
                    next.responseChannel.trySend(ResponseEnvelope(next.request.callId, ServiceCallStatus.Timeout, ByteArray(0)))
                } else {
                    calls += next
                }
                next = outgoingCalls.tryReceive().getOrNull()
            }

            calls.forEach { outgoingCalls.trySend(it) }
        }
    }

    /**
     * Called in the scope of [run].
     */
    open fun connectionError(ex: Exception) {
        errorHandler?.connectionError(ex)
        ex.printStackTrace()
    }

    suspend fun timeout() {
        while (scope.isActive) {
            val cutoff = vmNowMicro() - callTimeout

            pendingCalls
                .filter { it.value.createdMicros < cutoff }
                .toList()
                .forEach { (callId, call) ->
                    pendingCalls.remove(callId)
                    call.responseChannel.trySend(ResponseEnvelope(callId, ServiceCallStatus.Timeout, ByteArray(0)))
                }

            delay(1_000)
        }
    }

    override suspend fun <T> call(serviceName: String, funName: String, payload: ByteArray, decoder: ProtoDecoder<T>): T =
        OutgoingCall(RequestEnvelope(UUID(), serviceName, funName, payload)).let { outgoingCall ->

            outgoingLock.use {
                outgoingCalls.send(outgoingCall)
            }
            if (trace) println("outgoing call: $serviceName.$funName")

            val responseEnvelope = outgoingCall.responseChannel.receive()
            if (trace) println("response for $serviceName.$funName\n${responseEnvelope.dump()}")

            when (responseEnvelope.status) {
                ServiceCallStatus.Ok -> {
                    decoder.decodeProto(ProtoMessage(responseEnvelope.payload))
                }

                ServiceCallStatus.Timeout -> {
                    timeoutError(serviceName, funName, responseEnvelope)
                    throw RuntimeException("$serviceName  $funName  ${responseEnvelope.status}")
                }

                else -> {
                    responseError(serviceName, funName, responseEnvelope)
                    throw RuntimeException("$serviceName  $funName  ${responseEnvelope.status}")
                }
            }
        }

    /**
     * Called in the scope of the service caller. This function MUST throw an exception, otherwise the
     * service call will be hanging forever.
     */
    open fun timeoutError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {
        errorHandler?.callError(serviceName, funName, responseEnvelope)
        throw ServiceTimeoutException(serviceName, funName, responseEnvelope)
    }

    /**
     * Called in the scope of the service caller. This function MUST throw an exception, otherwise the
     * service call will be hanging forever.
     */
    open fun responseError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {
        errorHandler?.callError(serviceName, funName, responseEnvelope)
        throw ServiceResultException(serviceName, funName, responseEnvelope)
    }

}