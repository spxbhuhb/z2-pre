package hu.simplexion.z2.history.impl

import hu.simplexion.z2.auth.context.ensureAll
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.history.api.HistoryApi
import hu.simplexion.z2.history.model.HistoryEntry
import hu.simplexion.z2.history.model.HistoryFlags
import hu.simplexion.z2.history.table.HistoryEntryTable.Companion.historyEntryTable
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.services.ServiceImpl
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import org.slf4j.LoggerFactory

class HistoryImpl : HistoryApi, ServiceImpl<HistoryImpl> {

    companion object {
        val historyImpl = HistoryImpl().internal
        val errorLog = LoggerFactory.getLogger("Error")
        val businessLog = LoggerFactory.getLogger("Business")
        val securityLog = LoggerFactory.getLogger("Security")
        val technicalLog = LoggerFactory.getLogger("Technical")
        val systemLog = LoggerFactory.getLogger("System")
        val settingLog = LoggerFactory.getLogger("Setting")
    }

    override suspend fun list(flags: Int, start: Instant, end: Instant, limit: Int): List<HistoryEntry> {
        ensureAll(securityOfficerRole)
        return historyEntryTable.list(flags, start, end, limit)
    }

    // ------------------------------------------------------------------------------------------
    // Internal functions
    // ------------------------------------------------------------------------------------------

    fun add(
        createdBy: UUID<Principal>?,
        flags: Int,
        topic: LocalizedText,
        verb: LocalizedText,
        content: String
    ) {
        add(createdBy, flags, topic, verb, UUID.NIL, "text/plain", content)
    }

    fun add(
        createdBy: UUID<Principal>?,
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
                this.createdAt = now() // FIXME schematic instant initialization (using program start time)
                this.flags = flags
                this.topic = topic.key
                this.verb = verb.key
                @Suppress("UNCHECKED_CAST")
                this.subject = subject as? UUID<Any>
                this.contentType = contentType
                this.textContent = content
            }.also {
                val message = "[${topic.key.substringAfterLast('/').padEnd(30)}]  ${verb.key.substringAfterLast('/').padEnd(20)}  ${it.textContent}"
                when {
                    (flags and HistoryFlags.SECURITY) != 0 -> securityLog.info(message)
                    (flags and HistoryFlags.TECHNICAL) != 0 -> technicalLog.info(message)
                    (flags and HistoryFlags.BUSINESS) != 0 -> businessLog.info(message)
                    (flags and HistoryFlags.SETTING) != 0 -> settingLog.info(message)
                }
            }
        )
    }
}