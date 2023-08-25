import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import hu.simplexion.z2.auth.authJvm
import hu.simplexion.z2.history.historyJvm
import hu.simplexion.z2.i18n.i18nJvm
import hu.simplexion.z2.service.ktor.server.basicWebsocketServiceCallTransport
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.jetbrains.exposed.sql.Database
import java.time.Duration

fun main() {
    db()
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun db() {
    val config = HikariConfig().apply {
        jdbcUrl         = System.getenv("DB_JDBC_URL")
        driverClassName = System.getenv("DB_DRIVER_CLASS_NAME")
        username        = System.getenv("DB_USER_NAME")
        password        = System.getenv("DB_PASSWORD")
        maximumPoolSize = 10
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}

fun Application.module() {

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(20)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    historyJvm()
    authJvm()
    i18nJvm()

    routing {
        basicWebsocketServiceCallTransport("/z2/services")
    }
}