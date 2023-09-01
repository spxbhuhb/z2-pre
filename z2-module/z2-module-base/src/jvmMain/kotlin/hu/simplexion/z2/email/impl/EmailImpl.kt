package hu.simplexion.z2.email.impl

import hu.simplexion.z2.auth.context.ensureTechnicalAdmin
import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.event.Z2Event
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.content.model.Content
import hu.simplexion.z2.email.api.EmailApi
import hu.simplexion.z2.email.model.Email
import hu.simplexion.z2.email.model.EmailQuery
import hu.simplexion.z2.email.model.EmailQueueEntry
import hu.simplexion.z2.email.model.EmailSettings
import hu.simplexion.z2.email.table.EmailQueueTable.Companion.emailQueueTable
import hu.simplexion.z2.email.table.EmailTable.Companion.emailTable
import hu.simplexion.z2.email.worker.EmailQueued
import hu.simplexion.z2.service.runtime.ServiceImpl
import hu.simplexion.z2.worker.runtime.WorkerRuntime.Companion.workerRuntime
import org.jetbrains.exposed.sql.insert

class EmailImpl : EmailApi, ServiceImpl<EmailImpl> {

    companion object {
        val emailImpl = EmailImpl()
    }

    override suspend fun send(recipients: String, subject: String, contentType: String, contentText: String, attachments: List<UUID<Content>>) {
        val email = Email().also {
            it.recipients = recipients
            it.subject = subject
            it.contentType = contentType
            it.contentText = contentText
        }

        val uuid = emailTable.insert(email)
        emailQueueTable.insert(uuid)

        EmailQueued(uuid).fire()
    }

    override suspend fun query(query: EmailQuery): List<Email> {
        ensureTechnicalAdmin()
        return emailTable.query(query)
    }

    override suspend fun queryQueueSize(): Long {
        ensureTechnicalAdmin()
        return emailQueueTable.size()
    }

    override suspend fun queryQueue(limit: Int, offset : Long): List<EmailQueueEntry> {
        ensureTechnicalAdmin()
        return emailQueueTable.list(limit, offset)
    }

    override suspend fun cancel(email: UUID<Email>) {
        TODO("Not yet implemented")
    }

    override suspend fun getSettings(): EmailSettings {
        TODO("Not yet implemented")
    }

    override suspend fun setSettings(settings: EmailSettings) {
        TODO("Not yet implemented")
    }
}