package hu.simplexion.z2.content.impl.upload

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.encodeInto
import hu.simplexion.z2.commons.util.toLong
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.min


/**
 * An upload process. Created by `ContentImpl` and handled by [UploadManager].
 *
 * UploadStart lifecycle:
 *
 * ```text
 *    start
 *    pause - system restart, move node etc.
 *    start
 *    pause - system restart, move node etc.
 *    ...
 *    start
 *    close
 *```
 *
 * @param   size     The size of the complete content. The upload is done when there is
 *                   only one entry in [notes] with an offset of `0` and length equals
 *                   [size].
 */
class Upload(
    val uuid: UUID<Upload>,
    val size: Long,
    val dataPath: Path,
    val statusPath: Path
) {

    val ready = AtomicBoolean(false)

    lateinit var dataFile : RandomAccessFile
    lateinit var statusFile : RandomAccessFile

    /**
     * Account of uploaded chunks. Updated whenever a chunk is added to the upload.
     */
    var notes = listOf<ChunkNote>()

    val isComplete
        get() = (notes.size == 1 && notes.first().offset == 0L && notes.first().length == size)

    fun start() {
        dataFile = RandomAccessFile(dataPath.toFile(), "rw")
        statusFile = RandomAccessFile(statusPath.toFile(), "rw")
        if (dataFile.length() < size) dataFile.setLength(size)
        loadStatus()
        ready.set(true)
    }

    fun pause() {
        dataFile.close()
        statusFile.close()
    }

    fun close(sha256 : String) {
        if (!isComplete) abort()
        // FIXME sha if (sha256 != dataFile.sha256()) abort()

        Files.deleteIfExists(statusPath)
    }

    fun add(chunk: ChunkData) {
        if (! ready.get()) abort()
        if (chunk.offset + chunk.data.size > size) abort()

        dataFile.seek(chunk.offset)
        dataFile.write(chunk.data, 0, chunk.data.size)

        addNote(chunk.offset, chunk.data.size.toLong())
    }

    fun addNote(offset: Long, size: Long) {
        val all = (notes + ChunkNote(offset, size)).sortedBy { it.offset }
        val merged = mutableListOf<ChunkNote>()

        for (note in all) {
            if (merged.isEmpty()) {
                merged += note
                continue
            }

            val last = merged.last()

            if (last.offset + last.length == note.offset) {
                merged[merged.size - 1] = ChunkNote(last.offset, last.length + note.length)
            } else {
                merged += note
            }
        }

        notes = merged
        saveStatus()
    }

    fun saveStatus() {
        val bytes = ByteArray(notes.size * 16)
        var offset = 0
        for (note in notes) {
            note.offset.encodeInto(bytes, offset)
            note.length.encodeInto(bytes, offset + 8)
            offset += 16
        }

        if (statusFile.length() != bytes.size.toLong()) {
            statusFile.setLength(bytes.size.toLong())
        }

        statusFile.seek(0)
        statusFile.write(bytes)
    }

    fun loadStatus() {
        val length = statusFile.length()
        check(length < Int.MAX_VALUE)
        val bytes = ByteArray(length.toInt())

        statusFile.seek(0)
        statusFile.readFully(bytes)

        val result = mutableListOf<ChunkNote>()
        var offset = 0

        while (offset < length) {
            result += ChunkNote(bytes.toLong(offset), bytes.toLong(offset + 8))
            offset += 16
        }

        notes = result
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun RandomAccessFile.sha256() : String {
        val digest = MessageDigest.getInstance("SHA-256")

        var remaining = length()

        val bufferSize = min(remaining, 1024L*1024L)
        val bytes = ByteArray(bufferSize.toInt())

        seek(0)
        while (remaining > 0) {
            val readSize = min(bufferSize, remaining).toInt()
            val actualRead = read(bytes, 0, readSize)
            digest.update(bytes, 0, actualRead)
            remaining -= actualRead
        }

        return Base64.encode(digest.digest())
    }

    fun abort(throwException : Boolean = true) {
        safeCleanup { dataFile.close() }
        safeCleanup { statusFile.close() }
        safeCleanup { Files.deleteIfExists(dataPath) }
        safeCleanup { Files.deleteIfExists(statusPath) }
        if (throwException) throw UploadAbortException()
    }

    fun safeCleanup(block : () -> Unit) {
        try {
            block()
        } catch (ex : Exception) {
            // TODO logging
            ex.printStackTrace()
        }
    }
}