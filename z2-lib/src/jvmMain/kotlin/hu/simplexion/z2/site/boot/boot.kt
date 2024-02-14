package hu.simplexion.z2.site.boot

import hu.simplexion.z2.auth.authJvm
import hu.simplexion.z2.content.contentJvm
import hu.simplexion.z2.email.emailJvm
import hu.simplexion.z2.history.historyJvm
import hu.simplexion.z2.ktor.session
import hu.simplexion.z2.ktor.sessionWebsocketServiceCallTransport
import hu.simplexion.z2.localization.localizationJvm
import hu.simplexion.z2.setting.dsl.setting
import hu.simplexion.z2.setting.settingJvm
import hu.simplexion.z2.site.impl.SiteSettings
import hu.simplexion.z2.site.siteJvm
import hu.simplexion.z2.strictId.strictIdJvm
import hu.simplexion.z2.worker.workerJvm
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.time.Duration

/**
 * Scope for housekeeping background tasks. Cancelled during shutdown.
 */
val housekeepingScope = CoroutineScope(Dispatchers.IO)

lateinit var applicationEngine: ApplicationEngine

object KtorSettings {
    /**
     * The port on which Ktor accepts incoming connection. This is intentionally different from `SITE_PORT`,
     * the port visible from the outside. `SITE_PORT` has nothing to do with the actual port Ktor runs on,
     * it is quite usual to have proxies, firewalls, relays in the middle.
     */
    val port by setting<Int> { "INTERNAL_PORT" }
}

fun bootJvm(earlyConfig: Application.() -> Unit, siteConfig: Application.() -> Unit) {
    embeddedServer(Netty, port = KtorSettings.port, module = { module(earlyConfig, siteConfig) }).also {
        applicationEngine = it
        it.start(wait = true)
    }
}

fun Application.module(earlyConfig: Application.() -> Unit, siteConfig: Application.() -> Unit) {

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(20)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    historyJvm()
    earlyConfig()

    authJvm()
    localizationJvm()
    settingJvm()
    strictIdJvm()
    workerJvm()
    siteJvm()
    contentJvm()
    emailJvm()

    siteConfig()

    routing {
        session()
        sessionWebsocketServiceCallTransport("/z2/services")
        staticFiles("/", SiteSettings.static.toFile()) {
            this.default("index.html")
        }
    }
}