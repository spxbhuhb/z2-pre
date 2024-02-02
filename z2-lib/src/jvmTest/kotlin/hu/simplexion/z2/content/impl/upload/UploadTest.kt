package hu.simplexion.z2.content.impl.upload

import hu.simplexion.z2.util.UUID
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UploadTest {

//    @Test
//    fun basic() {
//        val bytes = "Hello World".encodeToByteArray()
//        val (upload, sha256) = upload(bytes)
//
//        upload.add(ChunkData(upload.uuid, 0, bytes))
//
//        upload.assertSuccess(bytes, sha256)
//    }
//
//    @Test
//    fun multiChunk() {
//        val chunks = listOf("Hello", " ", "World!").map { it.encodeToByteArray() }
//        var bytes = ByteArray(0)
//        chunks.forEach { bytes += it }
//
//        val (upload, sha256) = upload(bytes)
//
//        var offset = 0L
//        chunks.forEach {
//            upload.add(ChunkData(upload.uuid, offset, it))
//            offset += it.size
//        }
//
//        upload.assertSuccess(bytes, sha256)
//    }
//
//    @OptIn(ExperimentalEncodingApi::class)
//    fun upload(bytes: ByteArray): Pair<Upload, String> {
//        val uuid = UUID<Upload>()
//        val sha256 = Base64.encode(MessageDigest.getInstance("SHA-256").digest(bytes))
//        val dataPath = Paths.get("./var/tmp/$uuid.blob")
//        val statusPath = Paths.get("./var/tmp/$uuid.status")
//
//        val upload = Upload(
//            uuid,
//            bytes.size.toLong(),
//            dataPath,
//            statusPath
//        )
//
//        upload.start()
//
//        assertTrue(Files.exists(statusPath))
//        assertEquals(0L, Files.size(statusPath))
//
//        return upload to sha256
//    }
//
//    fun Upload.assertSuccess(bytes: ByteArray, sha256: String) {
//        close(sha256)
//
//        assertEquals(bytes.size.toLong(), Files.size(dataPath))
//        assertTrue(! Files.exists(statusPath))
//    }
}