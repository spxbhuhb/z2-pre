package hu.simplexion.z2.serialization

import hu.simplexion.z2.serialization.json.JsonBufferWriter
import hu.simplexion.z2.serialization.json.JsonDecoder
import hu.simplexion.z2.serialization.json.JsonEncoder
import hu.simplexion.z2.serialization.json.JsonMessageBuilder
import hu.simplexion.z2.serialization.json.elements.JsonElement
import hu.simplexion.z2.serialization.json.elements.JsonObject
import hu.simplexion.z2.serialization.protobuf.ProtoDecoder
import hu.simplexion.z2.serialization.protobuf.ProtoEncoder
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder

data class A(
    var b: Boolean = false,
    var i: Int = 0,
    var s: String = "",
    var l: MutableList<Int> = mutableListOf()
) {
    companion object : ProtoEncoder<A>, ProtoDecoder<A>, JsonEncoder<A>, JsonDecoder<A> {

        override fun decodeProto(message: ProtoMessage?): A {
            if (message == null) return A()

            return A(
                message.boolean(1),
                message.int(2),
                message.string(3),
                message.intList(4)
            )
        }

        override fun encodeProto(value: A): ByteArray =
            ProtoMessageBuilder()
                .boolean(1, value.b)
                .int(2, value.i)
                .string(3, value.s)
                .intList(4, value.l)
                .pack()

        override fun decodeJson(element: JsonElement?): A {
            if (element == null) return A()
            require(element is JsonObject) { "invalid JSON format"}
            val entries = element.entries

            return A(
                entries["b"]!!.asBoolean,
                entries["i"]!!.asInt,
                entries["s"]!!.asString,
                entries["l"]!!.asIntList
            )
        }

        override fun encodeJson(writer: JsonBufferWriter, value: A) {
            JsonMessageBuilder()
                .boolean("b", value.b)
                .int("i", value.i)
                .string("s", value.s)
                .intList("l", value.l)
                .pack()
        }
    }
}