package hu.simplexion.z2.content.api

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.content.model.ContentType
import hu.simplexion.z2.service.Service

interface ContentTypeApi : Service {
    suspend fun list() : List<ContentType>
    suspend fun get(uuid : UUID<ContentType>) : ContentType
    suspend fun add(contentType: ContentType)
    suspend fun update(contentType: ContentType)
}