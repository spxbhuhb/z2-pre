package hu.simplexion.z2.site.util

import hu.simplexion.z2.auth.authJvm
import hu.simplexion.z2.email.emailJvm
import hu.simplexion.z2.exposed.dbFromEnvironment
import hu.simplexion.z2.history.historyJvm
import hu.simplexion.z2.i18n.i18nJvm
import hu.simplexion.z2.ktor.session
import hu.simplexion.z2.ktor.sessionWebsocketServiceCallTransport
import hu.simplexion.z2.setting.settingJvm
import hu.simplexion.z2.site.siteJvm
import hu.simplexion.z2.strictId.strictIdJvm
import hu.simplexion.z2.worker.workerJvm
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.time.Duration

fun boot(siteConfig: Application.() -> Unit) {
    dbFromEnvironment()
    // TODO document Z2_SITE_PORT and mention that it is INTENTIONALLY different from SiteSettings.port
    // the port visible from the outside has nothing to do with the actual port Netty runs on, it is quite
    // usual to have proxies, firewalls, relays in the middle
    val port = System.getenv("Z2_SITE_PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, module = { module(siteConfig) }).start(wait = true)
}

fun Application.module(siteConfig: Application.() -> Unit) {

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(20)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    historyJvm()
    authJvm()
    i18nJvm()
    settingJvm()
    strictIdJvm()
    workerJvm()
    siteJvm()
    emailJvm()

    siteConfig()

    routing {
        session()
        sessionWebsocketServiceCallTransport("/z2/services")
    }
}