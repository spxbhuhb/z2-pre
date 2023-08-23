package hu.simplexion.z2.exposed

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.runtime.ServiceImpl
import hu.simplexion.z2.service.runtime.defaultServiceImplFactory
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

fun h2Test(vararg tables: Table) {
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    transaction {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
}

fun debugSql(active: Boolean) {
    val logger = LoggerFactory.getLogger("Exposed") as Logger
    logger.level = if (active) Level.DEBUG else Level.INFO
}

inline fun withTransaction(wrappedService: () -> ServiceImpl): ServiceImpl =
    ExposedTransactionWrapper(wrappedService())

fun registerWithTransaction(vararg services: ServiceImpl) {
    for (service in services) {
        defaultServiceImplFactory += withTransaction { service }
    }
}

fun <T> java.util.UUID.z2() =
    UUID<T>(this.toString())

val UUID<*>.jvm
    get() = java.util.UUID.fromString(this.toString())