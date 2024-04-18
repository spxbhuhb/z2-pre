package hu.simplexion.z2.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import hu.simplexion.z2.setting.dsl.SettingFlag.sensitive
import hu.simplexion.z2.setting.dsl.setting
import org.jetbrains.exposed.sql.Database

/**
 * Settings for a database connection.
 */
class JdbcSettings {

    /**
     * The JDBC_URL to connect. Examples:
     *
     * ```
     * jdbc:postgresql://127.0.0.1/z2site
     * jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;
     * ```
     */
    val jdbcUrl by setting<String> { "DB_JDBC_URL" }

    /**
     * Class name of the JDBC driver. Examples:
     *
     * ```
     * org.postgresql.Driver
     * org.h2.Driver
     * ```
     */
    val driverClassName by setting<String> { "DB_DRIVER_CLASS_NAME" }

    /**
     * Username to use for the database connection.
     */
    val username by setting<String> { "DB_USER" }

    /**
     * Password to use for the database connection.
     */
    val password by setting<String> { "DB_PASSWORD" } .. sensitive

    /**
     * Connects the default Exposed database to the one specified by these settings.
     */
    fun connect() {
        val config = HikariConfig().also {
            it.jdbcUrl = jdbcUrl
            it.driverClassName = driverClassName
            it.username = username
            it.password = password
            it.maximumPoolSize = 10
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }
}