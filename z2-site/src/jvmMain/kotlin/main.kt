import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import hu.simplexion.z2.auth.auth
import hu.simplexion.z2.history.history
import hu.simplexion.z2.i18n.i18n
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
        jdbcUrl         = "jdbc:postgresql://10.173.6.22/p1150"
        driverClassName = "org.postgresql.Driver"
        username        = "p1150"
        password        = "1aTYrppzDY9JGmY"
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

    history()
    auth()
    i18n()

    routing {
        basicWebsocketServiceCallTransport("/z2/services")
    }
}