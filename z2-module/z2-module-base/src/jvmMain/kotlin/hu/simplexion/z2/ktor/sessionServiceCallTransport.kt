package hu.simplexion.z2.ktor

import hu.simplexion.z2.auth.impl.SessionImpl.Companion.activeSessions
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.runtime.BasicServiceContext
import hu.simplexion.z2.service.runtime.ServiceContext
import hu.simplexion.z2.service.runtime.defaultServiceImplFactory
import hu.simplexion.z2.service.runtime.transport.RequestEnvelope
import hu.simplexion.z2.service.runtime.transport.ResponseEnvelope
import hu.simplexion.z2.service.runtime.transport.ServiceCallStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlin.collections.set

fun Routing.session() {
    get("/z2/session") {
        val sessionUuid = call.request.cookies["Z2_SESSION"] ?: UUID<Session>().toString()
        // FIXME session cookie settings
        call.response.cookies.append("Z2_SESSION", sessionUuid, httpOnly = true, path = "/")
        call.respond(HttpStatusCode.OK)
    }
}

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
                    if (ex is AccessDeniedException) {
                        ResponseEnvelope(
                            requestEnvelope.callId,
                            ServiceCallStatus.AccessDenied,
                            byteArrayOf()
                        )
                    } else {
                        ex.printStackTrace() // TODO proper logging
                        ResponseEnvelope(
                            requestEnvelope.callId,
                            ServiceCallStatus.Exception,
                            ProtoMessageBuilder().string(1, ex::class.simpleName ?: "").pack()
                        )
                    }
                }

                send(Frame.Binary(true, ResponseEnvelope.encodeProto(responseEnvelope)))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}