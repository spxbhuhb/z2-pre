package hu.simplexion.z2.history.table

import hu.simplexion.z2.auth.table.PrincipalTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.history.model.HistoryEntry
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select

open class HistoryEntryTable(
    principalTable: PrincipalTable
) : SchematicUuidTable<HistoryEntry>(
    "z2_history_entry",
    HistoryEntry()
) {

    companion object {
        val historyEntryTable = HistoryEntryTable(principalTable)
    }

    val createdAt = timestamp("createdAt")
    val createdBy = reference("createdBy", principalTable).nullable()
    val flags = integer("flags")
    val topic = varchar("topic", 120)
    val verb = varchar("verb", 120)
    val subject = uuid("subject").nullable()
    val contentType = varchar("contentType", 120)
    val textContent = text("textContent")

    fun list(inFlags : Int, start: Instant, end: Instant, limit: Int) : List<HistoryEntry> =
        select { createdAt greaterEq start }
            .andWhere { createdAt lessEq end }
            .andWhere { (flags bitwiseAnd inFlags) neq 0 }
            .limit(limit)
            .map {
                it.toSchematic(this, newInstance())
            }
}