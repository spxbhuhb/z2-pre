package hu.simplexion.z2.commons.protobuf

data class B(
    var a: A = A(),
    var s: String = ""
) {
    companion object : ProtoEncoder<B>, ProtoDecoder<B> {

        override fun decodeProto(message: ProtoMessage?): B {
            if (message == null) return B()

            return B(
                message.instance(1, A),
                message.string(2)
            )
        }

        override fun encodeProto(value: B): ByteArray =
            ProtoMessageBuilder()
                .instance(1, A, value.a)
                .string(2, value.s)
                .pack()
    }
}