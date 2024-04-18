package hu.simplexion.z2.email.table

import hu.simplexion.z2.email.model.Email
import hu.simplexion.z2.email.model.EmailQueueEntry
import hu.simplexion.z2.email.table.EmailTable.Companion.emailTable
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.exposed.z2
import hu.simplexion.z2.util.UUID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

open class EmailQueueTable(
    emailTable: EmailTable
) : Table(
    "z2_email_queue",
) {

    companion object {
        val emailQueueTable = EmailQueueTable(emailTable)
    }

    val email = reference("email", emailTable)
    val tries = integer("tries")
    val lastTry = timestamp("lastTry").nullable()
    val lastFailMessage = text("lastFailMessage").nullable()

    fun insert(entry: EmailQueueEntry) {
        insert {
            it[email] = entry.email.jvm
            it[tries] = entry.tries
            it[lastTry] = entry.lastTry
            it[lastFailMessage] = entry.lastFailMessage
        }
    }

    fun update(entry: EmailQueueEntry) {
        update({ email eq entry.email.jvm }) {
            it[tries] = entry.tries
            it[lastTry] = entry.lastTry
            it[lastFailMessage] = entry.lastFailMessage
        }
    }

    fun remove(uuid: UUID<Email>) {
        deleteWhere { email eq uuid.jvm }
    }

    fun size(): Long =
        selectAll().count()

    fun list(limit: Int? = null, offset: Long? = null): List<EmailQueueEntry> {
        var statement = selectAll()

        if (limit != null) {
            statement = if (offset != null) statement.limit(limit, offset) else statement.limit(limit)
        }

        return statement.map { row ->
            EmailQueueEntry().also {
                it.email = row[email].z2()
                it.lastTry = row[lastTry]
                it.tries = row[tries]
                it.lastFailMessage = row[lastFailMessage]
            }
        }
    }

}