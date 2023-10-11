package hu.simplexion.z2.history.impl

import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.api.HistoryApi
import hu.simplexion.z2.history.model.HistoryEntry
import hu.simplexion.z2.history.table.HistoryEntryTable.Companion.historyEntryTable
import hu.simplexion.z2.localization.locales.localized
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.service.ServiceImpl
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
        topic: LocalizedText,
        verb: LocalizedText,
        content: String
    ) {
        add(createdBy, flags, topic, verb, UUID.NIL, "text/plain", content)
    }

    fun add(
        createdBy: UUID<AccountPrivate>?,
        flags: Int,
        topic: LocalizedText,
        verb : LocalizedText,
        subject: UUID<*>?,
        contentType: String,
        content: String
    ) {
        historyEntryTable.insert(
            HistoryEntry().apply {
                this.createdBy = createdBy
                this.flags = flags
                this.topic = topic.key
                this.verb = verb.key
                @Suppress("UNCHECKED_CAST")
                this.subject = subject as? UUID<Any>
                this.contentType = contentType
                this.textContent = content
            }.also {
                println("${it.createdAt.localized}  [${it.topic.padEnd(30)}]  ${it.verb.padEnd(20)}  ${it.textContent}")
            }
        )
    }
}