package hu.simplexion.z2.serialization.json

import hu.simplexion.z2.serialization.A
import hu.simplexion.z2.serialization.B
import hu.simplexion.z2.serialization.E
import hu.simplexion.z2.util.UUID

class JsonMessageBuilderTest {

    val booleanVal = true
    val intVal = 123
    val longVal = 1234L
    val stringVal = "abc"
    val byteArrayVal = byteArrayOf(9, 8, 7)
    val uuidVal = UUID<Any>()
    val instanceVal = A(true, 12, "hello")
    val enumVal = E.V1

    val booleanListVal = listOf(true, false, true)
    val intListVal = listOf(1, 2, 3)
    val longListVal = listOf(1L, 2L, 3L, 4L)
    val stringListVal = listOf("a", "b", "c")
    val byteArrayListVal = listOf(byteArrayOf(1), byteArrayOf(2), byteArrayOf(3))
    val uuidListVal = listOf(UUID<Any>(), UUID(), UUID())
    val enumListVal = listOf(E.V2, E.V1)

    val instanceListVal = listOf(
        B(A(true, 123, "a", mutableListOf(1, 2, 3)), "AA"),
        B(A(false, 456, "b", mutableListOf(4, 5, 6)), "BB"),
        B(A(true, 789, "c", mutableListOf(7, 8, 9)), "CC")
    )

    class FieldName(
        var value: Int = 1
    ) {
        val next: String
            get() = "f${value ++}"
    }
//
//    @Test
//    fun testBuiltins() {
//        val fieldName = FieldName()
//
//        val builder = JsonMessageBuilder()
//            .boolean(fieldName.next, booleanVal)
//            .booleanOrNull(fieldName.next, booleanVal)
//            .booleanOrNull(fieldName.next, null)
//
//            .int(fieldName.next, intVal)
//            .intOrNull(fieldName.next, intVal)
//            .intOrNull(fieldName.next, null)
//
//            .long(fieldName.next, longVal)
//            .longOrNull(fieldName.next, longVal)
//            .longOrNull(fieldName.next, null)
//
//            .string(fieldName.next, stringVal)
//            .stringOrNull(fieldName.next, stringVal)
//            .stringOrNull(fieldName.next, null)
//
//            .byteArray(fieldName.next, byteArrayVal)
//            .byteArrayOrNull(fieldName.next, byteArrayVal)
//            .byteArrayOrNull(fieldName.next, null)
//
//            .uuid(fieldName.next, uuidVal)
//            .uuidOrNull(fieldName.next, uuidVal)
//            .uuidOrNull(fieldName.next, null)
//
//            .instance(fieldName.next, A, instanceVal)
//            .instanceOrNull(fieldName.next, A, instanceVal)
//            .instanceOrNull(fieldName.next, A, null)
//
//            .booleanList(fieldName.next, emptyList())
//            .booleanList(fieldName.next, booleanListVal)
//            .booleanListOrNull(fieldName.next, booleanListVal)
//            .booleanListOrNull(fieldName.next, null)
//
//            .intList(fieldName.next, emptyList())
//            .intList(fieldName.next, intListVal)
//            .intListOrNull(fieldName.next, intListVal)
//            .intListOrNull(fieldName.next, null)
//
//            .longList(fieldName.next, emptyList())
//            .longList(fieldName.next, longListVal)
//            .longListOrNull(fieldName.next, longListVal)
//            .longListOrNull(fieldName.next, null)
//
//            .stringList(fieldName.next, emptyList())
//            .stringList(fieldName.next, stringListVal)
//            .stringListOrNull(fieldName.next, stringListVal)
//            .stringListOrNull(fieldName.next, null)
//
//            .byteArrayList(fieldName.next, emptyList())
//            .byteArrayList(fieldName.next, byteArrayListVal)
//            .byteArrayListOrNull(fieldName.next, byteArrayListVal)
//            .byteArrayListOrNull(fieldName.next, null)
//
//            .uuidList(fieldName.next, emptyList())
//            .uuidList(fieldName.next, uuidListVal)
//            .uuidListOrNull(fieldName.next, uuidListVal)
//            .uuidListOrNull(fieldName.next, null)
//
//            .instanceList(fieldName.next, B, emptyList())
//            .instanceList(fieldName.next, B, instanceListVal)
//            .instanceListOrNull(fieldName.next, B, instanceListVal)
//            .instanceListOrNull(fieldName.next, B, null)
//
//        val wireformat = builder.pack()
//        val message = ProtoMessage(wireformat)
//        println(wireformat.dumpProto())
//
//        fieldName = 1
//
//        assertEquals(booleanVal, message.boolean(fieldName.next))
//        assertEquals(booleanVal, message.booleanOrNull(fieldName.next))
//        assertEquals(null, message.booleanOrNull(fieldName.next))
//
//        assertEquals(intVal, message.int(fieldName.next))
//        assertEquals(intVal, message.intOrNull(fieldName.next))
//        assertEquals(null, message.intOrNull(fieldName.next))
//
//        assertEquals(longVal, message.long(fieldName.next))
//        assertEquals(longVal, message.longOrNull(fieldName.next))
//        assertEquals(null, message.longOrNull(fieldName.next))
//
//        assertEquals(stringVal, message.string(fieldName.next))
//        assertEquals(stringVal, message.stringOrNull(fieldName.next))
//        assertEquals(null, message.stringOrNull(fieldName.next))
//
//        assertContentEquals(byteArrayVal, message.byteArray(fieldName.next))
//        assertContentEquals(byteArrayVal, message.byteArrayOrNull(fieldName.next))
//        assertEquals(null, message.byteArrayOrNull(fieldName.next))
//
//        assertEquals(uuidVal, message.uuid(fieldName.next))
//        assertEquals(uuidVal, message.uuidOrNull<Any>(fieldName.next))
//        assertEquals(null, message.uuidOrNull<Any>(fieldName.next))
//
//        assertEquals(instanceVal, message.instance(fieldName.next, A))
//        assertEquals(instanceVal, message.instanceOrNull(fieldName.next, A))
//        assertEquals(null, message.instanceOrNull(fieldName.next, A))
//
//        assertContentEquals(emptyList(), message.booleanList(fieldName.next))
//        assertContentEquals(booleanListVal, message.booleanList(fieldName.next))
//        assertContentEquals(booleanListVal, message.booleanListOrNull(fieldName.next))
//        assertEquals(null, message.booleanListOrNull(fieldName.next))
//
//        assertContentEquals(emptyList(), message.intList(fieldName.next))
//        assertContentEquals(intListVal, message.intList(fieldName.next))
//        assertContentEquals(intListVal, message.intListOrNull(fieldName.next))
//        assertEquals(null, message.intListOrNull(fieldName.next))
//
//        assertContentEquals(emptyList(), message.longList(fieldName.next))
//        assertContentEquals(longListVal, message.longList(fieldName.next))
//        assertContentEquals(longListVal, message.longListOrNull(fieldName.next))
//        assertEquals(null, message.longListOrNull(fieldName.next))
//
//        assertContentEquals(emptyList(), message.stringList(fieldName.next))
//        assertContentEquals(stringListVal, message.stringList(fieldName.next))
//        assertContentEquals(stringListVal, message.stringListOrNull(fieldName.next))
//        assertEquals(null, message.stringListOrNull(fieldName.next))
//
//        assertContentEquals(emptyList(), message.byteArrayList(fieldName.next))
//        message.byteArrayList(fieldName.next).forEachIndexed { index, bytes ->
//            assertContentEquals(byteArrayListVal[index], bytes)
//        }
//        assertNotNull(
//            message.byteArrayListOrNull(fieldName.next)
//        ).forEachIndexed { index, bytes ->
//            assertContentEquals(byteArrayListVal[index], bytes)
//        }
//        assertEquals(null, message.byteArrayListOrNull(fieldName.next))
//
//        assertContentEquals(emptyList<UUID<Any>>(), message.uuidList(fieldName.next))
//        assertContentEquals(uuidListVal, message.uuidList(fieldName.next))
//        assertContentEquals(uuidListVal, message.uuidListOrNull<Any>(fieldName.next))
//        assertEquals(null, message.uuidListOrNull<Any>(fieldName.next))
//
//        assertContentEquals(emptyList(), message.instanceList(fieldName.next, B))
//        assertContentEquals(instanceListVal, message.instanceList(fieldName.next, B))
//        assertContentEquals(instanceListVal, message.instanceListOrNull(fieldName.next, B))
//        assertEquals(null, message.instanceListOrNull(fieldName.next, B))
//    }
//
//    fun <T> ProtoDecoder<T>.decode(build: ProtoMessageBuilder.() -> Unit): T =
//        decodeProto(ProtoMessage(ProtoMessageBuilder().apply { build() }.pack()))
//
//    @Test
//    fun oneTest() {
//        assertEquals(Unit, ProtoOneUnit.decode { })
//
//        assertEquals(booleanVal, ProtoOneBoolean.decode { boolean(1, booleanVal) })
//        assertEquals(null, ProtoOneBooleanOrNull.decode { booleanOrNull(1, 2, null) })
//        assertEquals(booleanVal, ProtoOneBooleanOrNull.decode { booleanOrNull(1, 2, booleanVal) })
//
//        assertEquals(intVal, ProtoOneInt.decode { int(1, intVal) })
//        assertEquals(null, ProtoOneIntOrNull.decode { intOrNull(1, 2, null) })
//        assertEquals(intVal, ProtoOneIntOrNull.decode { intOrNull(1, 2, intVal) })
//
//        assertEquals(longVal, ProtoOneLong.decode { long(1, longVal) })
//        assertEquals(null, ProtoOneLongOrNull.decode { longOrNull(1, 2, null) })
//        assertEquals(longVal, ProtoOneLongOrNull.decode { longOrNull(1, 2, longVal) })
//
//        assertEquals(stringVal, ProtoOneString.decode { string(1, stringVal) })
//        assertEquals(null, ProtoOneStringOrNull.decode { stringOrNull(1, 2, null) })
//        assertEquals(stringVal, ProtoOneStringOrNull.decode { stringOrNull(1, 2, stringVal) })
//
//        assertContentEquals(byteArrayVal, ProtoOneByteArray.decode { byteArray(1, byteArrayVal) })
//        assertEquals(null, ProtoOneByteArrayOrNull.decode { byteArrayOrNull(1, 2, null) })
//        assertContentEquals(byteArrayVal, ProtoOneByteArrayOrNull.decode { byteArrayOrNull(1, 2, byteArrayVal) })
//
//        assertEquals(uuidVal, ProtoOneUuid.decode { uuid(1, uuidVal) })
//        assertEquals(null, ProtoOneUuidOrNull.decode { uuidOrNull(1, 2, null) })
//        assertEquals(uuidVal, ProtoOneUuidOrNull.decode { uuidOrNull(1, 2, uuidVal) })
//
//        assertEquals(instanceVal, ProtoOneInstance(A).decode { instance(1, A, instanceVal) })
//        assertEquals(null, ProtoOneInstanceOrNull(A).decode { instanceOrNull(1, 2, A, null) })
//        assertEquals(instanceVal, ProtoOneInstanceOrNull(A).decode { instanceOrNull(1, 2, A, instanceVal) })
//
//        assertEquals(enumVal, ProtoOneEnum(E.entries).decode { int(1, enumVal.ordinal) })
//        assertEquals(null, ProtoOneEnumOrNull(E.entries).decode { intOrNull(1, 2, null) })
//        assertEquals(enumVal, ProtoOneEnumOrNull(E.entries).decode { intOrNull(1, 2, enumVal.ordinal) })
//
//        assertContentEquals(booleanListVal, ProtoOneBooleanList.decode { booleanList(1, booleanListVal) })
//        assertEquals(null, ProtoOneBooleanListOrNull.decode { booleanOrNull(1, 2, null) })
//        assertEquals(booleanListVal, ProtoOneBooleanListOrNull.decode { booleanListOrNull(1, 2, booleanListVal) })
//
//        assertContentEquals(intListVal, ProtoOneIntList.decode { intList(1, intListVal) })
//        assertEquals(null, ProtoOneIntListOrNull.decode { intOrNull(1, 2, null) })
//        assertEquals(intListVal, ProtoOneIntListOrNull.decode { intListOrNull(1, 2, intListVal) })
//
//        assertContentEquals(longListVal, ProtoOneLongList.decode { longList(1, longListVal) })
//        assertEquals(null, ProtoOneLongListOrNull.decode { longOrNull(1, 2, null) })
//        assertEquals(longListVal, ProtoOneLongListOrNull.decode { longListOrNull(1, 2, longListVal) })
//
//        assertContentEquals(stringListVal, ProtoOneStringList.decode { stringList(1, stringListVal) })
//        assertEquals(null, ProtoOneStringListOrNull.decode { stringOrNull(1, 2, null) })
//        assertEquals(stringListVal, ProtoOneStringListOrNull.decode { stringListOrNull(1, 2, stringListVal) })
//
//        ProtoOneByteArrayList.decode { byteArrayList(1, byteArrayListVal) }.forEachIndexed { index, bytes ->
//            assertContentEquals(byteArrayListVal[index], bytes)
//        }
//        assertEquals(null, ProtoOneByteArrayListOrNull.decode { byteArrayListOrNull(1, 2, null) })
//        ProtoOneByteArrayList.decode { byteArrayListOrNull(1, 2, byteArrayListVal) }.forEachIndexed { index, bytes ->
//            assertContentEquals(byteArrayListVal[index], bytes)
//        }
//
//        assertContentEquals(uuidListVal, ProtoOneUuidList.decode { uuidList(1, uuidListVal) })
//        assertEquals(null, ProtoOneUuidListOrNull.decode { uuidOrNull(1, 2, null) })
//        assertEquals(uuidListVal, ProtoOneUuidListOrNull.decode { uuidListOrNull(1, 2, uuidListVal) })
//
//        assertContentEquals(instanceListVal, ProtoOneInstanceList(B).decode { instanceList(1, B, instanceListVal) })
//        assertEquals(null, ProtoOneInstanceListOrNull(B).decode { instanceOrNull(1, 2, B, null) })
//        assertEquals(instanceListVal, ProtoOneInstanceListOrNull(B).decode { instanceListOrNull(1, 2, B, instanceListVal) })
//
//        assertEquals(enumListVal, ProtoOneEnumList(E.entries).decode { intList(1, enumListToOrdinals(enumListVal)) })
//        assertEquals(null, ProtoOneEnumListOrNull(E.entries).decode { intListOrNull(1, 2, null) })
//        assertEquals(enumListVal, ProtoOneEnumListOrNull(E.entries).decode { intListOrNull(1, 2, enumListToOrdinals(enumListVal)) })
//    }
}