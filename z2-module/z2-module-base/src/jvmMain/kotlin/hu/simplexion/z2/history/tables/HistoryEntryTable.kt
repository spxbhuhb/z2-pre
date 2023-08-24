package hu.simplexion.z2.history.tables

import hu.simplexion.z2.auth.tables.AccountPrivateTable
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.history.model.HistoryEntry
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select

open class HistoryEntryTable(
    accountPrivateTable: AccountPrivateTable
) : SchematicUuidTable<HistoryEntry>(
    "history_entry",
    HistoryEntry()
) {

    val createdAt = timestamp("createdAt")
    val createdBy = reference("createdBy", accountPrivateTable)
    val createdFor = uuid("createdFor")
    val flags = integer("flags")
    val contentType = varchar("contentType", 120)
    val content = text("content")

    fun list(inFlags : Int, start: Instant, end: Instant, limit: Int) : List<HistoryEntry> =
        select { createdAt greaterEq start }
            .andWhere { createdAt lessEq end }
            .andWhere { (flags bitwiseAnd inFlags) neq 0 }
            .limit(limit)
            .map {
                it.toSchematic(this, newInstance())
            }
}