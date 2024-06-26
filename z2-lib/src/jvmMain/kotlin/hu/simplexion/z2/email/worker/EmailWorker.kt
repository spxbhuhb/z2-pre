package hu.simplexion.z2.email.worker

import hu.simplexion.z2.deprecated.event.AnonymousEventListener
import hu.simplexion.z2.deprecated.event.EventCentral
import hu.simplexion.z2.deprecated.event.Z2Event
import hu.simplexion.z2.email.emailBusHandle
import hu.simplexion.z2.email.model.Email
import hu.simplexion.z2.email.model.EmailSettings
import hu.simplexion.z2.email.table.EmailQueueTable.Companion.emailQueueTable
import hu.simplexion.z2.email.table.EmailTable.Companion.emailTable
import hu.simplexion.z2.site.impl.SiteImpl.Companion.siteImpl
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.localLaunch
import hu.simplexion.z2.worker.model.BackgroundWorker
import hu.simplexion.z2.worker.model.WorkerRegistration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

// TODO make email worker "multi-thread"
class EmailWorker(
    override val registration: UUID<WorkerRegistration>
) : BackgroundWorker {

    companion object {
        val settings = EmailSettings() // FIXME per-instance e-mail settings
    }

    val eventListener = AnonymousEventListener(emailBusHandle, ::onBusEvent)

    val normalQueue = Channel<UUID<Email>>(Channel.UNLIMITED)

    override suspend fun run(job: Job) {
        EventCentral.attach(eventListener)

        localLaunch { retry(job) }

        loadNormalQueue()

        try {
            for (uuid in normalQueue) {
                if (! job.isActive) return
                if (sendPending() == 0) {
                    delay(500)
                    sendPending()
                }
            }
        } catch (ex : Exception) {
            ex.printStackTrace() // FIXME email processing exceptions
        } finally {
            EventCentral.detach(eventListener)
        }
    }

    fun loadNormalQueue() {
        transaction {
            emailQueueTable.list().filter { it.lastTry == null }.forEach { normalQueue.trySend(it.email) }
        }
    }

    fun sendPending() : Int {
        val pending = transaction {
            emailQueueTable.list().filter { it.lastTry == null }.map { it.email }
        }

        pending.forEach {
            try {
                send(it)
            } catch (ex: CancellationException) {
                return 0
            }
        }

        return pending.size
    }

    suspend fun retry(job: Job) {
        while (job.isActive) {
            val uuids = transaction { emailQueueTable.list().filter { it.lastTry != null }.map { it.email } }

            if (uuids.isEmpty()) {
                delay(settings.retryCheckInterval)
                continue
            }

            for (uuid in uuids) {
                if (! job.isActive) return

                try {
                    send(uuid)
                } catch (ex: CancellationException) {
                    return
                } catch (ex: Exception) {
                    job.cancel()
                }
            }
        }
    }

    fun onBusEvent(event: Z2Event) {
        if (event !is EmailQueued) return
        normalQueue.trySend(event.uuid) // FIXME handle channel overflow
    }

    private fun send(uuid: UUID<Email>) {
        val email = transaction { emailTable.get(uuid) }

        try {
            val prop = Properties()

            prop["mail.smtp.auth"] = settings.auth
            prop["mail.smtp.starttls.enable"] = if (settings.tls) "true" else "false"
            prop["mail.smtp.host"] = settings.host
            prop["mail.smtp.port"] = settings.port
            prop["mail.smtp.protocol"] = settings.protocol
            prop["mail.debug"] = settings.debug

            val session = Session.getInstance(prop, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(settings.username, settings.password)
                }
            })

            val message = MimeMessage(session)
            message.setFrom(InternetAddress(settings.username))

            val recipient = InternetAddress.parse(siteImpl.testEmail() ?: email.recipients)

            message.setRecipients(Message.RecipientType.TO, recipient)
            message.setSubject(email.subject, "utf-8")

            val multipart = MimeMultipart()
            val bodyPart = MimeBodyPart()
            when (email.contentType) {
                "text/plain" -> bodyPart.setText(email.contentText, "UTF-8")
                "text/html" -> bodyPart.setContent(email.contentText, "text/html; charset=UTF-8")
                else -> throw IllegalStateException("body mime type (${email.contentType} must be 'text/plain' or 'text/html}")
            }
            bodyPart.disposition = Part.INLINE
            multipart.addBodyPart(bodyPart)

            message.setContent(multipart)

            Transport.send(message)

            transaction {
                emailTable.sent(uuid)
                emailQueueTable.remove(uuid)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            // TODO retry
        }
    }

}