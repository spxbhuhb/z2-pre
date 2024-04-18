package hu.simplexion.z2.serialization.protobuf

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
    companion object : ProtoEncoder<A>, ProtoDecoder<A> {

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
    }
}