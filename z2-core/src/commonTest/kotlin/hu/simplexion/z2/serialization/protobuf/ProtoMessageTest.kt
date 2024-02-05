package hu.simplexion.z2.serialization.protobuf

import hu.simplexion.z2.serialization.A
import hu.simplexion.z2.serialization.B
import hu.simplexion.z2.util.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ProtoMessageTest {

    @Test
    fun testPrimitives() {
        singlePrimitive(true, { boolean(1, it) }) { boolean(1) }
        singlePrimitive(false, { boolean(1, it) }) { boolean(1) }

        singlePrimitive(123, { int(1, it) }) { int(1) }
        singlePrimitive(- 123, { int(1, it) }) { int(1) }

        singlePrimitive(123, { long(1, it) }) { long(1) }
        singlePrimitive(- 123, { long(1, it) }) { long(1) }

        singlePrimitive("hello", { string(1, it) }) { string(1) }

        singlePrimitive("hello".encodeToByteArray(), { byteArray(1, it) }) { byteArray(1) }

        singlePrimitive(UUID<Any>(), { uuid(1, it) }) { uuid(1) }
    }

    @Test
    fun testBooleanList() {
        val expected = listOf(true, false, true)
        val wireFormat = ProtoMessageBuilder().booleanList(1, expected).pack()
        val message = ProtoMessage(wireFormat)
        val actual = message.booleanList(1)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testIntList() {
        val expected = listOf(1, 4, 7)
        val wireFormat = ProtoMessageBuilder().intList(1, expected).pack()
        val message = ProtoMessage(wireFormat)
        val actual = message.intList(1)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testLongList() {
        val expected = listOf(1L, 4L, 7L)
        val wireFormat = ProtoMessageBuilder().longList(1, expected).pack()
        val message = ProtoMessage(wireFormat)
        val actual = message.longList(1)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testStringList() {
        val expected = listOf("a", "b", "c")
        val wireFormat = ProtoMessageBuilder().stringList(1, expected).pack()
        val message = ProtoMessage(wireFormat)
        val actual = message.stringList(1)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testByteArrayList() {
        val expected = listOf(byteArrayOf(1), byteArrayOf(2), byteArrayOf(3))
        val wireFormat = ProtoMessageBuilder().byteArrayList(1, expected).pack()
        val message = ProtoMessage(wireFormat)
        val actual = message.byteArrayList(1)
        for (i in expected.indices) {
            assertContentEquals(expected[i], actual[i])
        }
    }

    @Test
    fun testUuidList() {
        val expected = listOf<UUID<Any>>(UUID(), UUID(), UUID())
        val wireFormat = ProtoMessageBuilder().uuidList(1, expected).pack()
        val message = ProtoMessage(wireFormat)
        val actual = message.uuidList<Any>(1)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testSimpleClass() =
        instance(A, A) { A(true, 12, "hello", mutableListOf(4, 5, 6)) }

    @Test
    fun testNestedClass() =
        instance(B, B) { B(A(true, 123, "a", mutableListOf(4, 5, 6)), "b") }

    @Test
    fun testListOfNestedClasses() {
        val expected = listOf(
            B(A(true, 123, "a", mutableListOf(1, 2, 3)), "aa"),
            B(A(false, 456, "b", mutableListOf(4, 5, 6)), "bb"),
            B(A(true, 789, "c", mutableListOf(7, 8, 9)), "cc")
        )

        val wireFormat = ProtoMessageBuilder().instanceList(1, B, expected).pack()
        val message = ProtoMessage(wireFormat)
        val actual = message.instanceList(1, B)
        assertContentEquals(expected, actual)
    }

    // ----------------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------------

    fun <T> singlePrimitive(expected: T, write: ProtoMessageBuilder.(value: T) -> Unit, read: ProtoMessage.() -> T) {
        val wireFormat = ProtoMessageBuilder().apply { write(expected) }.pack()
        val message = ProtoMessage(wireFormat)

        assertEquals(1, message.records.size)
        val actual = message.read()
        if (expected is ByteArray && actual is ByteArray) {
            assertContentEquals(expected, actual)
        } else {
            assertEquals(expected, actual)
        }
    }

    fun <T> instance(encoder: ProtoEncoder<T>, decoder: ProtoDecoder<T>, builder: () -> T) {
        val expected = builder()
        val wireFormat = encoder.encodeProto(expected)
        val message = ProtoMessage(wireFormat)
        val actual = decoder.decodeProto(message)
        assertEquals(expected, actual)
    }
}