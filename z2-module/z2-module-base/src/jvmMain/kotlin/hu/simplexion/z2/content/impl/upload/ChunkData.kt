package hu.simplexion.z2.content.impl.upload

import hu.simplexion.z2.commons.util.UUID

/**
 * An actual chunk as sent by the client. This is a short living class, exists until the data is
 * written out to the permanent storage, discarded after.
 */
class ChunkData(
    val uuid: UUID<*>,
    val offset : Long,
    val data : ByteArray
)