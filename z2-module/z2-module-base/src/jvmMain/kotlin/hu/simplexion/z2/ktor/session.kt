package hu.simplexion.z2.ktor

import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.commons.util.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.session() {
    get("/z2/session") {
        val sessionUuid = call.request.cookies["Z2_SESSION"] ?: UUID<Session>().toString()
        // FIXME session cookie settings
        call.response.cookies.append("Z2_SESSION", sessionUuid, httpOnly = true, path = "/")
        call.respond(HttpStatusCode.OK)
    }
}