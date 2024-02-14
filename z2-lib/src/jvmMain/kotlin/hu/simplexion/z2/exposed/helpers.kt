package hu.simplexion.z2.exposed

import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.defaultServiceImplFactory
import hu.simplexion.z2.util.UUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun h2Test(vararg tables: Table) {
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    transaction {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
}

inline fun withTransaction(wrappedService: () -> ServiceImpl<*>) =
    ExposedTransactionWrapper(wrappedService())

fun <T> EntityID<java.util.UUID>.z2() =
    UUID<T>(this.value.mostSignificantBits, this.value.leastSignificantBits)

fun <T> java.util.UUID.z2() =
    UUID<T>(this.mostSignificantBits, this.leastSignificantBits)

val UUID<*>.jvm: java.util.UUID
    get() = java.util.UUID(this.msb, this.lsb)

/**
 * Call [SchemaUtils.createMissingTablesAndColumns] to create and/or update the tables
 * passed as parameters.
 */
fun tables(vararg tables: Table): Array<out Table> {
    transaction {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
    return tables
}

/**
 * Returns with true if all the [tables] are empty, false otherwise.
 */
fun isAllEmpty(vararg tables: Table): Boolean =
    transaction {
        for (table in tables) {
            if (table.isNotEmpty()) return@transaction false
        }
        true
    }

fun implementations(vararg implementations: ServiceImpl<*>) {
    for (implementation in implementations) {
        defaultServiceImplFactory += withTransaction { implementation }
    }
}

fun Table.isEmpty() = (selectAll().count() == 0L)

fun Table.isNotEmpty() = (selectAll().count() != 0L)

fun Boolean.alsoTransactionIf(block: (it: Boolean) -> Unit): Boolean {
    transaction { block(this@alsoTransactionIf) }
    return this
}