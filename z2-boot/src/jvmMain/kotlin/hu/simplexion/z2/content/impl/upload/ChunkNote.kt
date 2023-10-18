package hu.simplexion.z2.content.impl.upload

/**
 * A note of a saved chunk. [Upload] uses this class to keep principal of the uploaded data chunks.
 */
class ChunkNote(
    val offset : Long,
    val length: Long
)