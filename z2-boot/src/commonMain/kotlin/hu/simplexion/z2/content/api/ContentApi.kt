package hu.simplexion.z2.content.api

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.content.model.Content
import hu.simplexion.z2.service.Service

interface ContentApi : Service {
    suspend fun uploadChunk(uuid: UUID<Content>, position : Long, bytes : ByteArray)
    suspend fun cancelUpload(uuid : UUID<Content>)
    suspend fun closeUpload(uuid : UUID<Content>, sha256 : String)
    suspend fun rename(uuid : UUID<Content>, name : String)
    suspend fun delete(uuid : UUID<Content>)
    suspend fun getDownloadLink(uuid: UUID<Content>) : String
}