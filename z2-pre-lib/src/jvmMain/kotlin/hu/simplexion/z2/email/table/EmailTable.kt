package hu.simplexion.z2.email.table

import hu.simplexion.z2.auth.table.PrincipalTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.email.model.Email
import hu.simplexion.z2.email.model.EmailQuery
import hu.simplexion.z2.email.model.EmailStatus
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.util.UUID
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll

open class EmailTable(
    principalTable: PrincipalTable
) : SchematicUuidTable<Email>(
    "z2_email",
    Email()
) {

    companion object {
        val emailTable = EmailTable(principalTable)
    }

    val createdAt = timestamp("createdAt")
    val createdBy = reference("createdBy", principalTable).nullable()

    val status = enumerationByName<EmailStatus>("status", 20)
    val sentAt = timestamp("sentAt").nullable()

    val sensitive = bool("sensitive")
    val hasAttachment = bool("hasAttachment")

    val recipients = text("recipients")
    val subject = text("subject")

    val contentType = varchar("contentType", 60)
    val contentText = text("contentText")

    fun status(inUuid: UUID<Email>, inStatus: EmailStatus) {
        update(inUuid) {
            it[status] = inStatus
            if (inStatus == EmailStatus.Sent) {
                it[sentAt] = now()
            }
        }
    }

    fun query(query : EmailQuery) : List<Email> {
        val statement = selectAll()
        query.sender?.let { statement.andWhere { createdBy eq it.jvm } }
        query.recipient?.let { statement.andWhere { recipients like "%$it%" } }
        query.after?.let { statement.andWhere { createdAt greaterEq it } }
        query.before?.let { statement.andWhere { createdAt lessEq it } }
        query.subject?.let { statement.andWhere { subject like "%$it%" } }
        query.hasAttachment?.let { statement.andWhere { hasAttachment eq it } }
        query.status?.let { statement.andWhere { status eq it } }

        statement.limit(query.limit, query.offset)

        return statement.map {
            it.toSchematic(this, Email()).apply {
                if (sensitive) {
                    contentText = "********"
                }
            }
        }
    }
}