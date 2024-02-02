package hu.simplexion.z2.content.impl.upload

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.content.impl.BasicPlacementStrategy
import hu.simplexion.z2.content.impl.ContentImpl
import hu.simplexion.z2.content.impl.ContentImpl.Companion.contentImpl
import hu.simplexion.z2.content.impl.ContentImpl.Companion.globalContentPlacementStrategy
import hu.simplexion.z2.content.model.Content
import hu.simplexion.z2.content.model.ContentStatus
import hu.simplexion.z2.content.table.ContentTable.Companion.contentTable
import hu.simplexion.z2.exposed.h2Test
import hu.simplexion.z2.services.BasicServiceContext
import hu.simplexion.z2.services.set
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ContentImplTest {

//    companion object {
//        @JvmStatic
//        @BeforeClass
//        fun beforeClass() {
//            h2Test(contentTable)
//        }
//    }
//
//    fun BasicServiceContext.impl() = ContentImpl().newInstance(this)
//
//    @OptIn(ExperimentalEncodingApi::class)
//    @Test
//    fun basic() {
//        globalContentPlacementStrategy = BasicPlacementStrategy(Paths.get("./var/tmp/"))
//
//        val bytes = "Hello World!".encodeToByteArray()
//        val sha256 = Base64.encode(MessageDigest.getInstance("SHA-256").digest(bytes))
//
//        val content = Content().apply {
//            name = "test.txt"
//            type = "plain/text"
//            size = bytes.size.toLong()
//        }
//
//        val upload = transaction { contentImpl.startUpload(content) }
//
//        @Suppress("UNCHECKED_CAST")
//        val contentUuid = upload.uuid as UUID<Content>
//
//        val context = BasicServiceContext()
//        context[upload.uuid] = upload
//
//        transaction { runBlocking { context.impl().uploadChunk(contentUuid, 0, bytes) } }
//        transaction { runBlocking { context.impl().closeUpload(contentUuid, sha256) } }
//
//        assertEquals(bytes.size.toLong(), Files.size(Paths.get("./var/tmp/$contentUuid.blob")))
//        assertTrue(! Files.exists(Paths.get("./var/tmp/$contentUuid.status")))
//
//        val readBack = transaction { contentTable[contentUuid] }
//
//        content.sha256 = sha256
//        assertEquals(contentUuid, readBack.uuid)
//        assertEquals(content.name, readBack.name)
//        assertEquals(content.sha256, readBack.sha256)
//        assertEquals(ContentStatus.Ready, readBack.status)
//    }
}