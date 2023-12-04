package hu.simplexion.z2.ktor

import hu.simplexion.z2.commons.protobuf.ProtoDecoder
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.vmNowMicro
import hu.simplexion.z2.service.ServiceErrorHandler
import hu.simplexion.z2.service.ServiceResultException
import hu.simplexion.z2.service.ServiceTimeoutException
import hu.simplexion.z2.service.transport.RequestEnvelope
import hu.simplexion.z2.service.transport.ResponseEnvelope
import hu.simplexion.z2.service.transport.ServiceCallStatus
import hu.simplexion.z2.service.transport.ServiceCallTransport
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

    val outgoingCalls = Channel<OutgoingCall>(Channel.UNLIMITED)
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
                client.webSocket(path) {

                    launch {
                        for (call in outgoingCalls) {
                            pendingCalls[call.request.callId] = call
                            send(Frame.Binary(true, RequestEnvelope.encodeProto(call.request)))
                        }
                    }

                    retryDelay = 200 // reset the retry delay as we have a working connection

                    for (frame in incoming) {

                        frame as? Frame.Binary ?: continue

                        val responseEnvelope = ResponseEnvelope.decodeProto(ProtoMessage(frame.data))

                        val call = pendingCalls.remove(responseEnvelope.callId)
                            ?: continue // TODO report unknown call ids

                        call.responseChannel.trySend(responseEnvelope)
                    }

                }
            } catch (ex: Exception) {
                connectionError(ex)
                delay(retryDelay) // wait a second before trying to re-establish the connection
                if (retryDelay < 5_000) retryDelay = (retryDelay * 115) / 100
            }
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

            outgoingCalls.send(outgoingCall)
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