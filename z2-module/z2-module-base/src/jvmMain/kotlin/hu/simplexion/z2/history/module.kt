package hu.simplexion.z2.history

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.history.impl.HistoryImpl.Companion.historyImpl
import hu.simplexion.z2.history.tables.HistoryEntryTable.Companion.historyEntryTable

fun historyJvm() {
    historyCommon()
    tables(historyEntryTable)
    implementations(historyImpl)
}