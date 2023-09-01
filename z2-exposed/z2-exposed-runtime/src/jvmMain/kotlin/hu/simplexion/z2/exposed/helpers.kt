package hu.simplexion.z2.exposed

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.runtime.ServiceImpl
import hu.simplexion.z2.service.runtime.defaultServiceImplFactory
import org.jetbrains.exposed.dao.id.EntityID
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

inline fun withTransaction(wrappedService: () -> ServiceImpl<*>) =
    ExposedTransactionWrapper(wrappedService())

fun <T> EntityID<java.util.UUID>.z2() =
    UUID<T>(this.value.mostSignificantBits, this.value.leastSignificantBits)

fun <T> java.util.UUID.z2() =
    UUID<T>(this.mostSignificantBits, this.leastSignificantBits)

val UUID<*>.jvm : java.util.UUID
    get() = java.util.UUID(this.msb, this.lsb)

fun tables(vararg tables : Table) {
    transaction {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
}

fun implementations(vararg implementations : ServiceImpl<*>) {
    for (implementation in implementations) {
        defaultServiceImplFactory += withTransaction { implementation }
    }
}