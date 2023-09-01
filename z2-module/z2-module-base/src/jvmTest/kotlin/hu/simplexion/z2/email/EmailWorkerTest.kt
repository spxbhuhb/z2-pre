package hu.simplexion.z2.email

import hu.simplexion.z2.email.impl.EmailImpl.Companion.emailImpl
import hu.simplexion.z2.email.model.EmailSettings
import hu.simplexion.z2.email.worker.EmailWorkerProvider
import hu.simplexion.z2.service.runtime.ServiceContext
import hu.simplexion.z2.setting.impl.SettingImpl.Companion.settingImpl
import hu.simplexion.z2.setting.util.CommonSettings
import hu.simplexion.z2.setting.util.CommonSettings.putSystemSettings
import hu.simplexion.z2.testing.integratedWithSo
import hu.simplexion.z2.worker.impl.WorkerImpl.Companion.workerImpl
import hu.simplexion.z2.worker.model.WorkerRegistration
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass
import org.subethamail.wiser.Wiser
import javax.mail.internet.MimeMultipart
import kotlin.test.Test
import kotlin.test.assertEquals

class EmailWorkerTest {

    companion object {

        val wiser = Wiser()

        @BeforeClass
        @JvmStatic
        fun setup() {
            wiser.setPort(2500) // Default is 25
            wiser.start()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            wiser.stop()
        }

    }

    @Test
    fun basic() {
        wiser.messages.clear()

        integratedWithSo(debugSql = true, withTransaction = false) { _, so ->
            prepare(so)

            transaction {
                runBlocking {
                    emailImpl(so).send("noreply@simplexion.hu", "Hello", contentText = "World")
                }
            }

            // withTimeout(1000) {
            while (wiser.messages.size == 0) {
                delay(30)
            }
            //  }

            assertEquals(1, wiser.messages.size)

            val message = wiser.messages.first()
            val mimeMessage = message.mimeMessage
            val content = mimeMessage.content as MimeMultipart

            assertEquals("Hello", mimeMessage.subject)
            assertEquals(1, content.count)

            val part = content.getBodyPart(0)

            assertEquals("text/plain; charset=UTF-8", part.contentType)
            assertEquals("World", part.content)
        }
    }

    suspend fun prepare(so: ServiceContext) {
        transaction {
            runBlocking {
                val workerUuid = workerImpl(so).add(WorkerRegistration().also {
                    it.provider = EmailWorkerProvider.PROVIDER_UUID
                    it.name = "Test Email Worker"
                    it.enabled = false
                })

                putSystemSettings(workerUuid, EmailSettings().also {
                    it.host = "localhost"
                    it.port = 2500
                    it.username = "noreply@simplexion.hu"
                    it.password = "helloworld"
                    it.protocol = "smtp"
                    it.tls = false
                    it.auth = true
                })

                workerImpl(so).enable(workerUuid)
            }
        }
    }

}