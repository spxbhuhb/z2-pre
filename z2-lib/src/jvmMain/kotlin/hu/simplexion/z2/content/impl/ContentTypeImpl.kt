package hu.simplexion.z2.content.impl

import hu.simplexion.z2.auth.context.ensureLoggedIn
import hu.simplexion.z2.auth.context.ensureSecurityOfficer
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.content.api.ContentTypeApi
import hu.simplexion.z2.content.model.ContentType
import hu.simplexion.z2.content.table.ContentTypeTable.Companion.contentTypeTable
import hu.simplexion.z2.schematic.ensureValid
import hu.simplexion.z2.services.ServiceImpl

open class ContentTypeImpl : ContentTypeApi, ServiceImpl<ContentTypeImpl> {

    companion object {
        val contentTypeImpl = ContentTypeImpl().internal
    }

    override suspend fun list(): List<ContentType> {
        ensureLoggedIn()
        return contentTypeTable.list()
    }

    override suspend fun get(uuid: UUID<ContentType>): ContentType {
        ensureLoggedIn()
        return contentTypeTable.get(uuid)
    }

    override suspend fun add(contentType: ContentType) {
        ensureSecurityOfficer()
        ensureValid(contentType, true)
        contentTypeTable.insert(contentType)
    }

    override suspend fun update(contentType: ContentType) {
        ensureSecurityOfficer()
        ensureValid(contentType)
        contentTypeTable.update(contentType.uuid, contentType)
    }
}