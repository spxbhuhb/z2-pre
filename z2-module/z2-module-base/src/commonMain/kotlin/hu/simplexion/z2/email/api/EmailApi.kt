package hu.simplexion.z2.email.api

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.content.model.Content
import hu.simplexion.z2.email.model.Email
import hu.simplexion.z2.email.model.EmailQuery
import hu.simplexion.z2.email.model.EmailQueueEntry
import hu.simplexion.z2.email.model.EmailSettings
import hu.simplexion.z2.service.runtime.Service

interface EmailApi : Service {

    /**
     * Send an e-mail from the system. This API is not meant for
     * use from the clients directly. Instead, it is typically
     * called from other service implementations to send e-mails
     * when an event happens.
     *
     * Requires technical administrator role or internal call.
     */
    suspend fun send(
        recipients: String,
        subject: String,
        content : String,
        contentMimeType : String = "text/plain",
        attachments : List<UUID<Content>>
    )

    /**
     * Query the e-mails known to the system.
     *
     * Requires technical administrator role.
     */
    suspend fun query(query : EmailQuery) : List<Email>

    /**
     * Query the size of the e-mail queue.
     *
     * Requires technical administrator role.
     */
    suspend fun queryQueueSize() : Long

    /**
     * Query the e-mail queue.
     *
     * Requires technical administrator role.
     */
    suspend fun queryQueue(limit : Int = 1000, offset : Long = 0) : List<EmailQueueEntry>

    /**
     * Stop the given e-mail if it hasn't been sent already.
     *
     * Requires technical administrator role or the account has to be the same
     * as the `createdBy` of the e-mail.
     */
    suspend fun cancel(email : UUID<Email>)

    /**
     * Get the settings of the e-mail subsystem.
     *
     * Requires technical administrator role.
     */
    suspend fun getSettings() : EmailSettings

    /**
     * Set the settings of the e-mail subsystem.
     */
    suspend fun setSettings(settings : EmailSettings)
}