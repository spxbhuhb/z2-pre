package hu.simplexion.z2.email.worker

import com.sun.mail.util.MailConnectException
import hu.simplexion.z2.application.applicationSettings
import hu.simplexion.z2.application.model.ApplicationMode
import hu.simplexion.z2.deprecated.event.AnonymousEventListener
import hu.simplexion.z2.deprecated.event.EventCentral
import hu.simplexion.z2.deprecated.event.Z2Event
import hu.simplexion.z2.email.emailBusHandle
import hu.simplexion.z2.email.model.EmailQueueEntry
import hu.simplexion.z2.email.model.EmailSettings
import hu.simplexion.z2.email.model.EmailStatus
import hu.simplexion.z2.email.table.EmailQueueTable.Companion.emailQueueTable
import hu.simplexion.z2.email.table.EmailTable.Companion.emailTable
import hu.simplexion.z2.setting.dsl.setting
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.localLaunch
import hu.simplexion.z2.worker.model.BackgroundWorker
import hu.simplexion.z2.worker.model.WorkerRegistration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import kotlin.time.Duration.Companion.minutes

// TODO make email worker "multi-thread"
class EmailWorker(
    override val registration: UUID<WorkerRegistration>
) : BackgroundWorker {

    companion object {
        val settings = EmailSettings() // FIXME per-instance e-mail settings
        val logger = LoggerFactory.getLogger(EmailWorker::class.java)!!
    }

    val eventListener = AnonymousEventListener(emailBusHandle, ::onBusEvent)

    val normalQueue = Channel<EmailQueueEntry>(Channel.UNLIMITED)

    override suspend fun run(job: Job) {
        EventCentral.attach(eventListener)

        localLaunch { retry(job) }

        loadNormalQueue()

        try {
            for (entry in normalQueue) {
                if (!job.isActive) return
                if (sendPending() == 0) {
                    delay(500)
                    sendPending()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace() // FIXME email processing exceptions
        } finally {
            EventCentral.detach(eventListener)
        }
    }

    fun loadNormalQueue() {
        transaction {
            emailQueueTable.list().filter { it.lastTry == null }.forEach { normalQueue.trySend(it) }
        }
    }

    fun sendPending(): Int {
        val pending = transaction {
            emailQueueTable.list().filter { it.lastTry == null }
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

            val now = Clock.System.now().minus(30.minutes)

            val entries = transaction {
                emailQueueTable.list().filter { entry ->
                    entry.lastTry?.let { it < now } ?: false
                }
            }

            if (entries.isEmpty()) {
                delay(settings.retryCheckInterval)
                continue
            }

            for (entry in entries) {
                if (!job.isActive) return

                try {
                    send(entry)
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
        normalQueue.trySend(event.entry) // FIXME handle channel overflow
    }

    private fun send(queueEntry: EmailQueueEntry, failMessage: String? = null) {
        val email = transaction { emailTable[queueEntry.email] }

        if (email.status in listOf(EmailStatus.Sent, EmailStatus.Failed)) {
            transaction {
                emailQueueTable.remove(email.uuid)
            }
            return
        }

        fun update(status: EmailStatus) {
            transaction {
                emailTable.status(queueEntry.email, status)
                if (status in listOf(EmailStatus.Sent, EmailStatus.Failed)) {
                    emailQueueTable.remove(queueEntry.email)
                    // FIXME store the fail message
                } else {
                    queueEntry.lastTry = Clock.System.now()
                    queueEntry.tries++
                    queueEntry.lastFailMessage = failMessage
                    emailQueueTable.update(queueEntry)
                }
            }
        }

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

            val address = if (applicationSettings.mode == ApplicationMode.Live) {
                email.recipients
            } else {
                checkNotNull(setting<String> { "TEST_EMAIL" }.value) { "test mode without TEST_EMAIL setting" }
            }

            val recipient = try {
                InternetAddress.parse(address)
            } catch (ex: AddressException) {
                logger.error("failed to send email ${email.uuid}", ex)
                update(EmailStatus.Failed)
                return
            }

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

            try {

                Transport.send(message)

                update(EmailStatus.Sent)
                logger.info("email sent: ${email.uuid}")

            } catch (ex: AuthenticationFailedException) {

                // config error, keep the e-mail in the queue
                logger.error("email server authentication fail", ex)
                update(EmailStatus.RetryWait)

            } catch (ex: MailConnectException) {

                // config error or server is unavailable, keep the e-mail in the queue
                logger.error("email server authentication fail", ex)
                update(EmailStatus.RetryWait)

            } catch (ex: SendFailedException) {

                logger.error("error message from the mail server for email ${email.uuid}", ex)
                update(EmailStatus.Failed)

            } catch (ex: MessagingException) {

                // whatever erroe, keep the e-mail in the queue
                logger.error("email send fail fail", ex)
                update(EmailStatus.RetryWait)

            }

        } catch (ex: Exception) {
            logger.error("failed to send email ${email.uuid}", ex)
            update(EmailStatus.Failed)
        }
    }

}