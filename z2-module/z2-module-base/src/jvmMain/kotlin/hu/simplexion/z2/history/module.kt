package hu.simplexion.z2.history

import hu.simplexion.z2.auth.tables.AccountPrivateTable
import hu.simplexion.z2.exposed.registerWithTransaction
import hu.simplexion.z2.history.impl.HistoryImpl
import hu.simplexion.z2.history.tables.HistoryEntryTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

lateinit var historyImpl: HistoryImpl

fun history() {

    historyCommon()

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            HistoryEntryTable(AccountPrivateTable())
        )
    }

    historyImpl = HistoryImpl()

    registerWithTransaction(
        historyImpl
    )

}