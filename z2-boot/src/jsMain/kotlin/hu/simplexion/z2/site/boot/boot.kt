package hu.simplexion.z2.site.boot

import hu.simplexion.z2.auth.authJs
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.auth.sessionService
import hu.simplexion.z2.history.historyJs
import hu.simplexion.z2.localization.localeJs
import hu.simplexion.z2.service.defaultServiceCallTransport
import hu.simplexion.z2.service.ktor.client.BasicWebSocketServiceTransport
import io.ktor.client.fetch.*
import kotlinx.browser.window
import kotlinx.coroutines.await

var session = Session()

suspend fun bootJs() {
    fetch("/z2/session").await()

    defaultServiceCallTransport = BasicWebSocketServiceTransport(
        window.location.origin.replace("http", "ws") + "/z2/services",
        false
    ).also {
        it.start()
    }

    localeJs()
    authJs()
    historyJs()

    sessionService.getSession()?.let { session = it }
}