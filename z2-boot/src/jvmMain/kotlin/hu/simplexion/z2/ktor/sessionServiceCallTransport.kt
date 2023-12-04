package hu.simplexion.z2.ktor

import hu.simplexion.z2.auth.context.AccessDenied
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.activeSessions
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.auth.util.AuthenticationFail
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.BasicServiceContext
import hu.simplexion.z2.service.ServiceContext
import hu.simplexion.z2.service.defaultServiceImplFactory
import hu.simplexion.z2.service.transport.RequestEnvelope
import hu.simplexion.z2.service.transport.ResponseEnvelope
import hu.simplexion.z2.service.transport.ServiceCallStatus
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlin.collections.set

fun Routing.sessionWebsocketServiceCallTransport(
    path: String = "/z2/service",
    newContext: (uuid: UUID<ServiceContext>) -> ServiceContext = { BasicServiceContext(it) }
) {
    webSocket(path) {
        try {
            val sessionUuid = call.request.cookies["Z2_SESSION"]?.let { UUID<ServiceContext>(it) } ?: UUID()

            val context = newContext(sessionUuid)

            activeSessions[sessionUuid]?.let {
                context.data[Session.SESSION_TOKEN_UUID] = it
            }

            for (frame in incoming) {
                frame as? Frame.Binary ?: continue

                // if this throws an exception there is an error in the service framework
                // better to close the connection then
                val requestEnvelope = RequestEnvelope.decodeProto(ProtoMessage(frame.data))

                val responseEnvelope = try {

                    val service = requireNotNull(defaultServiceImplFactory[requestEnvelope.serviceName, context])

                    val responseBuilder = ProtoMessageBuilder()

                    service.dispatch(
                        requestEnvelope.funName,
                        ProtoMessage(requestEnvelope.payload),
                        responseBuilder
                    )

                    ResponseEnvelope(
                        requestEnvelope.callId,
                        ServiceCallStatus.Ok,
                        responseBuilder.pack()
                    )

                } catch (ex: Exception) {
                    when (ex) {
                        is AccessDenied -> {
                            ResponseEnvelope(
                                requestEnvelope.callId,
                                ServiceCallStatus.AccessDenied,
                                byteArrayOf()
                            )
                        }

                        is AuthenticationFail -> {
                            ResponseEnvelope(
                                requestEnvelope.callId,
                                if (ex.locked) ServiceCallStatus.AuthenticationFailLocked else ServiceCallStatus.AuthenticationFail,
                                byteArrayOf()
                            )
                        }

                        else -> {
                            ex.printStackTrace() // TODO proper logging
                            ResponseEnvelope(
                                requestEnvelope.callId,
                                ServiceCallStatus.Exception,
                                ProtoMessageBuilder().string(1, ex::class.simpleName ?: "").pack()
                            )
                        }
                    }
                }

                send(Frame.Binary(true, ResponseEnvelope.encodeProto(responseEnvelope)))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}