package hu.simplexion.z2.email.table

import hu.simplexion.z2.email.model.EmailQueueEntry
import hu.simplexion.z2.email.table.EmailTable.Companion.emailTable
import hu.simplexion.z2.exposed.z2
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll

open class EmailQueueTable(
    emailTable: EmailTable
) : Table(
    "email_queue",
) {

    companion object {
        val emailQueueTable = EmailQueueTable(emailTable)
    }

    val email = reference("email", emailTable)
    val tries = integer("tries")
    val lastTry = timestamp("lastTry")
    val lastFailMessage = text("lastFailMessage")

    fun size() : Long =
        selectAll().count()

    fun list(limit : Int, offset : Long): List<EmailQueueEntry> =
        selectAll()
            .limit(limit, offset)
            .map { row ->
                EmailQueueEntry().also {
                    it.email = row[email].z2()
                    it.lastTry = row[lastTry]
                    it.tries = row[tries]
                    it.lastFailMessage = row[lastFailMessage]
                }
            }

}