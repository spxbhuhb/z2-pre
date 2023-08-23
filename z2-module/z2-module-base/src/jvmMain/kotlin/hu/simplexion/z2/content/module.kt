package hu.simplexion.z2.content

import hu.simplexion.z2.content.impl.ContentImpl
import hu.simplexion.z2.content.tables.ContentTable
import hu.simplexion.z2.exposed.registerWithTransaction
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

internal val contentTable = ContentTable("content")

fun content() {

    contentCommon()

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            contentTable
        )
    }

    registerWithTransaction(
        ContentImpl()
    )

}