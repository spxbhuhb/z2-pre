package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.serialization.protobuf.*
import hu.simplexion.z2.util.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProtoBuiltinTest {

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

    @Test
    fun testBuiltins() {
        var fieldNumber = 1

        fun <T> T.byTwo(): T {
            fieldNumber += 2
            return this
        }

        val builder = ProtoMessageBuilder()
            .boolean(fieldNumber++, booleanVal)
            .booleanOrNull(fieldNumber, fieldNumber + 1, booleanVal).byTwo()
            .booleanOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .int(fieldNumber++, intVal)
            .intOrNull(fieldNumber, fieldNumber + 1, intVal).byTwo()
            .intOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .long(fieldNumber++, longVal)
            .longOrNull(fieldNumber, fieldNumber + 1, longVal).byTwo()
            .longOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .string(fieldNumber++, stringVal)
            .stringOrNull(fieldNumber, fieldNumber + 1, stringVal).byTwo()
            .stringOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .byteArray(fieldNumber++, byteArrayVal)
            .byteArrayOrNull(fieldNumber, fieldNumber + 1, byteArrayVal).byTwo()
            .byteArrayOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .uuid(fieldNumber++, uuidVal)
            .uuidOrNull(fieldNumber, fieldNumber + 1, uuidVal).byTwo()
            .uuidOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .instance(fieldNumber++, A, instanceVal)
            .instanceOrNull(fieldNumber, fieldNumber + 1, A, instanceVal).byTwo()
            .instanceOrNull(fieldNumber, fieldNumber + 1, A, null).byTwo()

            .booleanList(fieldNumber++, emptyList())
            .booleanList(fieldNumber++, booleanListVal)
            .booleanListOrNull(fieldNumber, fieldNumber + 1, booleanListVal).byTwo()
            .booleanListOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .intList(fieldNumber++, emptyList())
            .intList(fieldNumber++, intListVal)
            .intListOrNull(fieldNumber, fieldNumber + 1, intListVal).byTwo()
            .intListOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .longList(fieldNumber++, emptyList())
            .longList(fieldNumber++, longListVal)
            .longListOrNull(fieldNumber, fieldNumber + 1, longListVal).byTwo()
            .longListOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .stringList(fieldNumber++, emptyList())
            .stringList(fieldNumber++, stringListVal)
            .stringListOrNull(fieldNumber, fieldNumber + 1, stringListVal).byTwo()
            .stringListOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .byteArrayList(fieldNumber++, emptyList())
            .byteArrayList(fieldNumber++, byteArrayListVal)
            .byteArrayListOrNull(fieldNumber, fieldNumber + 1, byteArrayListVal).byTwo()
            .byteArrayListOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .uuidList(fieldNumber++, emptyList())
            .uuidList(fieldNumber++, uuidListVal)
            .uuidListOrNull(fieldNumber, fieldNumber + 1, uuidListVal).byTwo()
            .uuidListOrNull(fieldNumber, fieldNumber + 1, null).byTwo()

            .instanceList(fieldNumber++, B, emptyList())
            .instanceList(fieldNumber++, B, instanceListVal)
            .instanceListOrNull(fieldNumber, fieldNumber + 1, B, instanceListVal).byTwo()
            .instanceListOrNull(fieldNumber, fieldNumber + 1, B, null).byTwo()

        val wireformat = builder.pack()
        val message = ProtoMessage(wireformat)
        println(wireformat.dumpProto())

        fieldNumber = 1

        assertEquals(booleanVal, message.boolean(fieldNumber++))
        assertEquals(booleanVal, message.booleanOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.booleanOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertEquals(intVal, message.int(fieldNumber++))
        assertEquals(intVal, message.intOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.intOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertEquals(longVal, message.long(fieldNumber++))
        assertEquals(longVal, message.longOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.longOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertEquals(stringVal, message.string(fieldNumber++))
        assertEquals(stringVal, message.stringOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.stringOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertContentEquals(byteArrayVal, message.byteArray(fieldNumber++))
        assertContentEquals(byteArrayVal, message.byteArrayOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.byteArrayOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertEquals(uuidVal, message.uuid(fieldNumber++))
        assertEquals(uuidVal, message.uuidOrNull<Any>(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.uuidOrNull<Any>(fieldNumber, fieldNumber + 1).byTwo())

        assertEquals(instanceVal, message.instance(fieldNumber++, A))
        assertEquals(instanceVal, message.instanceOrNull(fieldNumber, fieldNumber + 1, A).byTwo())
        assertEquals(null, message.instanceOrNull(fieldNumber, fieldNumber + 1, A).byTwo())

        assertContentEquals(emptyList(), message.booleanList(fieldNumber++))
        assertContentEquals(booleanListVal, message.booleanList(fieldNumber++))
        assertContentEquals(booleanListVal, message.booleanListOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.booleanListOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertContentEquals(emptyList(), message.intList(fieldNumber++))
        assertContentEquals(intListVal, message.intList(fieldNumber++))
        assertContentEquals(intListVal, message.intListOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.intListOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertContentEquals(emptyList(), message.longList(fieldNumber++))
        assertContentEquals(longListVal, message.longList(fieldNumber++))
        assertContentEquals(longListVal, message.longListOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.longListOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertContentEquals(emptyList(), message.stringList(fieldNumber++))
        assertContentEquals(stringListVal, message.stringList(fieldNumber++))
        assertContentEquals(stringListVal, message.stringListOrNull(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.stringListOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertContentEquals(emptyList(), message.byteArrayList(fieldNumber++))
        message.byteArrayList(fieldNumber++).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertNotNull(
            message.byteArrayListOrNull(fieldNumber, fieldNumber + 1).byTwo()
        ).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, message.byteArrayListOrNull(fieldNumber, fieldNumber + 1).byTwo())

        assertContentEquals(emptyList<UUID<Any>>(), message.uuidList(fieldNumber++))
        assertContentEquals(uuidListVal, message.uuidList(fieldNumber++))
        assertContentEquals(uuidListVal, message.uuidListOrNull<Any>(fieldNumber, fieldNumber + 1).byTwo())
        assertEquals(null, message.uuidListOrNull<Any>(fieldNumber, fieldNumber + 1).byTwo())

        assertContentEquals(emptyList(), message.instanceList(fieldNumber++, B))
        assertContentEquals(instanceListVal, message.instanceList(fieldNumber++, B))
        assertContentEquals(instanceListVal, message.instanceListOrNull(fieldNumber, fieldNumber + 1, B).byTwo())
        assertEquals(null, message.instanceListOrNull(fieldNumber, fieldNumber + 1, B).byTwo())
    }

    fun <T> ProtoDecoder<T>.decode(build: ProtoMessageBuilder.() -> Unit): T =
        decodeProto(ProtoMessage(ProtoMessageBuilder().apply { build() }.pack()))

    @Test
    fun oneTest() {
        assertEquals(Unit, ProtoOneUnit.decode { })

        assertEquals(booleanVal, ProtoOneBoolean.decode { boolean(1, booleanVal) })
        assertEquals(null, ProtoOneBooleanOrNull.decode { booleanOrNull(1, 2, null) })
        assertEquals(booleanVal, ProtoOneBooleanOrNull.decode { booleanOrNull(1, 2, booleanVal) })

        assertEquals(intVal, ProtoOneInt.decode { int(1, intVal) })
        assertEquals(null, ProtoOneIntOrNull.decode { intOrNull(1, 2, null) })
        assertEquals(intVal, ProtoOneIntOrNull.decode { intOrNull(1, 2, intVal) })

        assertEquals(longVal, ProtoOneLong.decode { long(1, longVal) })
        assertEquals(null, ProtoOneLongOrNull.decode { longOrNull(1, 2, null) })
        assertEquals(longVal, ProtoOneLongOrNull.decode { longOrNull(1, 2, longVal) })

        assertEquals(stringVal, ProtoOneString.decode { string(1, stringVal) })
        assertEquals(null, ProtoOneStringOrNull.decode { stringOrNull(1, 2, null) })
        assertEquals(stringVal, ProtoOneStringOrNull.decode { stringOrNull(1, 2, stringVal) })

        assertContentEquals(byteArrayVal, ProtoOneByteArray.decode { byteArray(1, byteArrayVal) })
        assertEquals(null, ProtoOneByteArrayOrNull.decode { byteArrayOrNull(1, 2, null) })
        assertContentEquals(byteArrayVal, ProtoOneByteArrayOrNull.decode { byteArrayOrNull(1, 2, byteArrayVal) })

        assertEquals(uuidVal, ProtoOneUuid.decode { uuid(1, uuidVal) })
        assertEquals(null, ProtoOneUuidOrNull.decode { uuidOrNull(1, 2, null) })
        assertEquals(uuidVal, ProtoOneUuidOrNull.decode { uuidOrNull(1, 2, uuidVal) })

        assertEquals(instanceVal, ProtoOneInstance(A).decode { instance(1, A, instanceVal) })
        assertEquals(null, ProtoOneInstanceOrNull(A).decode { instanceOrNull(1, 2, A, null) })
        assertEquals(instanceVal, ProtoOneInstanceOrNull(A).decode { instanceOrNull(1, 2, A, instanceVal) })

        assertEquals(enumVal, ProtoOneEnum(E.entries).decode { int(1, enumVal.ordinal) })
        assertEquals(null, ProtoOneEnumOrNull(E.entries).decode { intOrNull(1, 2, null) })
        assertEquals(enumVal, ProtoOneEnumOrNull(E.entries).decode { intOrNull(1, 2, enumVal.ordinal) })

        assertContentEquals(booleanListVal, ProtoOneBooleanList.decode { booleanList(1, booleanListVal) })
        assertEquals(null, ProtoOneBooleanListOrNull.decode { booleanOrNull(1, 2, null) })
        assertEquals(booleanListVal, ProtoOneBooleanListOrNull.decode { booleanListOrNull(1, 2, booleanListVal) })

        assertContentEquals(intListVal, ProtoOneIntList.decode { intList(1, intListVal) })
        assertEquals(null, ProtoOneIntListOrNull.decode { intOrNull(1, 2, null) })
        assertEquals(intListVal, ProtoOneIntListOrNull.decode { intListOrNull(1, 2, intListVal) })

        assertContentEquals(longListVal, ProtoOneLongList.decode { longList(1, longListVal) })
        assertEquals(null, ProtoOneLongListOrNull.decode { longOrNull(1, 2, null) })
        assertEquals(longListVal, ProtoOneLongListOrNull.decode { longListOrNull(1, 2, longListVal) })

        assertContentEquals(stringListVal, ProtoOneStringList.decode { stringList(1, stringListVal) })
        assertEquals(null, ProtoOneStringListOrNull.decode { stringOrNull(1, 2, null) })
        assertEquals(stringListVal, ProtoOneStringListOrNull.decode { stringListOrNull(1, 2, stringListVal) })

        ProtoOneByteArrayList.decode { byteArrayList(1, byteArrayListVal) }.forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertEquals(null, ProtoOneByteArrayListOrNull.decode { byteArrayListOrNull(1, 2, null) })
        ProtoOneByteArrayList.decode { byteArrayListOrNull(1, 2, byteArrayListVal) }.forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }

        assertContentEquals(uuidListVal, ProtoOneUuidList.decode { uuidList(1, uuidListVal) })
        assertEquals(null, ProtoOneUuidListOrNull.decode { uuidOrNull(1, 2, null) })
        assertEquals(uuidListVal, ProtoOneUuidListOrNull.decode { uuidListOrNull(1, 2, uuidListVal) })
        
        assertContentEquals(instanceListVal, ProtoOneInstanceList(B).decode { instanceList(1, B, instanceListVal) })
        assertEquals(null, ProtoOneInstanceListOrNull(B).decode { instanceOrNull(1, 2, B, null) })
        assertEquals(instanceListVal, ProtoOneInstanceListOrNull(B).decode { instanceListOrNull(1, 2, B, instanceListVal) })

        assertEquals(enumListVal, ProtoOneEnumList(E.entries).decode { intList(1, enumListToOrdinals(enumListVal)) })
        assertEquals(null, ProtoOneEnumListOrNull(E.entries).decode { intListOrNull(1, 2, null) })
        assertEquals(enumListVal, ProtoOneEnumListOrNull(E.entries).decode { intListOrNull(1, 2, enumListToOrdinals(enumListVal)) })
    }
}