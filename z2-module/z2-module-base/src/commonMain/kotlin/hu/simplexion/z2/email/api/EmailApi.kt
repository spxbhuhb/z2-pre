package hu.simplexion.z2.email.api

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.content.model.Content
import hu.simplexion.z2.email.model.Email
import hu.simplexion.z2.email.model.EmailQuery
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
    fun send(
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
    fun query(query : EmailQuery) : List<Email>

    /**
     * Stop the given e-mail if it hasn't been sent already.
     *
     * Requires technical administrator role or the account has to be the same
     * as the `createdBy` of the e-mail.
     */
    fun cancel(email : UUID<Email>)

    /**
     * Enable the e-mail worker, so it sends out e-mails. The system
     * remembers the status of the worker. If it is enabled before
     * a restart it will start automatically after the restart.
     *
     * Requires technical administrator role.
     */
    fun enableWorker()

    /**
     * Disable the e-mail worker, so it won't send e-mails.The system
     * remembers the status of the worker. If it is disabled before
     * a restart it will NOT start automatically after the restart.
     *
     * Requires technical administrator role.
     */
    fun disableWorker()

    /**
     * Get the settings of the e-mail subsystem.
     *
     * Requires technical administrator role.
     */
    fun getSettings() : EmailSettings

    /**
     * Set the settings of the e-mail subsystem.
     */
    fun setSettings(settings : EmailSettings)
}