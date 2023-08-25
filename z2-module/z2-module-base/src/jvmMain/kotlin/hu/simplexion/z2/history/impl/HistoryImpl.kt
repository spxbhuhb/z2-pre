package hu.simplexion.z2.history.impl

import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.auth.tables.AccountPrivateTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.api.HistoryApi
import hu.simplexion.z2.history.model.HistoryEntry
import hu.simplexion.z2.history.tables.HistoryEntryTable
import hu.simplexion.z2.history.tables.HistoryEntryTable.Companion.historyEntryTable
import hu.simplexion.z2.service.runtime.ServiceImpl
import kotlinx.datetime.Instant

class HistoryImpl : HistoryApi, ServiceImpl<HistoryImpl> {

    companion object {
        val historyImpl = HistoryImpl()
    }

    override suspend fun list(flags: Int, start: Instant, end: Instant, limit: Int): List<HistoryEntry> {
        ensure(securityOfficerRole)
        return historyEntryTable.list(flags, start, end, limit)
    }

    // ------------------------------------------------------------------------------------------
    // Internal functions
    // ------------------------------------------------------------------------------------------

    fun add(
        createdBy: UUID<AccountPrivate>?,
        flags: Int,
        content: String
    ) {
        add(createdBy, UUID.NIL, flags, "text/plain", content)
    }

    fun add(
        createdBy: UUID<AccountPrivate>?,
        createdFor: UUID<*>,
        flags: Int,
        contentType: String,
        content: String
    ) {
        historyEntryTable.insert(
            HistoryEntry().apply {
                this.createdBy = createdBy
                @Suppress("UNCHECKED_CAST")
                this.createdFor = createdFor as UUID<Any>
                this.flags = flags
                this.contentType = contentType
                this.content = content
            }
        )
    }
}