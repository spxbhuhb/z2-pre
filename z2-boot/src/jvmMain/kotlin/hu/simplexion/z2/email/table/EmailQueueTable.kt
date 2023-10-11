package hu.simplexion.z2.email.table

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.email.model.Email
import hu.simplexion.z2.email.model.EmailQueueEntry
import hu.simplexion.z2.email.table.EmailTable.Companion.emailTable
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.exposed.z2
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
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
    val lastTry = timestamp("lastTry").nullable()
    val lastFailMessage = text("lastFailMessage").nullable()

    fun insert(uuid: UUID<Email>) {
        insert {
            it[email] = uuid.jvm
            it[tries] = 0
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