package hu.simplexion.z2.content.table

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.content.model.Content
import hu.simplexion.z2.content.model.ContentStatus
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import org.jetbrains.exposed.sql.update

class ContentTable(
    tableName : String
) : SchematicUuidTable<Content>(
    tableName,
    Content()
) {

    companion object {
        val contentTable = ContentTable("z2_content")
    }

    val name = varchar("name", 1000)
    val type = varchar("type", 40)
    val size = long("size")
    val sha256 = varchar("sha256", 44).nullable()
    val status = enumerationByName<ContentStatus>("status", 20)

    fun cancelUpload(uuid: UUID<Content>) {
        update({ id eq uuid.jvm }) {
            it[this.status] = ContentStatus.Cancelled
        }
    }

    fun closeUpload(uuid: UUID<Content>, sha256: String) {
        update({ id eq uuid.jvm }) {
            it[this.sha256] = sha256
            it[this.status] = ContentStatus.Ready
        }
    }

}