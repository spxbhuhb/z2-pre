package hu.simplexion.z2.ktor

import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.services.ServiceContext
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.session() {
    get("/z2/session") {
        val existingSessionId = call.request.cookies["Z2_SESSION"]?.let { UUID<ServiceContext>(it) }

        val id = if (existingSessionId != null && sessionImpl.getSessionForContext(existingSessionId) != null) {
            existingSessionId
        } else {
            UUID()
        }.toString()

        call.response.cookies.append("Z2_SESSION", id, httpOnly = true, path = "/")
        call.respond(HttpStatusCode.OK)
    }
}