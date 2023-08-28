package hu.simplexion.z2.history.impl

import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.api.HistoryApi
import hu.simplexion.z2.history.model.HistoryEntry
import hu.simplexion.z2.history.table.HistoryEntryTable.Companion.historyEntryTable
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
        topic: String,
        content: String
    ) {
        add(createdBy, flags, topic, UUID.NIL, "text/plain", content)
    }

    fun add(
        createdBy: UUID<AccountPrivate>?,
        flags: Int,
        topic: String,
        subject: UUID<*>?,
        contentType: String,
        content: String
    ) {
        historyEntryTable.insert(
            HistoryEntry().apply {
                this.createdBy = createdBy
                this.flags = flags
                this.topic = topic
                @Suppress("UNCHECKED_CAST")
                this.subject = subject as? UUID<Any>
                this.contentType = contentType
                this.textContent = content
            }
        )
    }
}