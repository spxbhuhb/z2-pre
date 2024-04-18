package hu.simplexion.z2.content.table

import hu.simplexion.z2.content.model.ContentType
import hu.simplexion.z2.exposed.SchematicUuidTable

class ContentTypeTable(
    tableName : String
) : SchematicUuidTable<ContentType>(
    tableName,
    ContentType()
) {

    companion object {
        val contentTypeTable = ContentTypeTable("z2_content_type")
    }

    val extension = varchar("extension", 20)
    val mimeType = varchar("mimeType", 100)
    val sizeLimit = long("sizeLimit")
    val allowed = bool("allowed")

}