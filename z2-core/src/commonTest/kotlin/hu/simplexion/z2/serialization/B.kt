package hu.simplexion.z2.serialization

data class B(
    var a: A = A(),
    var s: String = ""
) {
    companion object : InstanceEncoder<B>, InstanceDecoder<B> {

        override fun encodeInstance(builder: MessageBuilder, value: B): ByteArray =
            builder
                .instance(1, "a", A, value.a)
                .string(2, "s", value.s)
                .pack()

        override fun decodeInstance(message: Message?): B {
            if (message == null) return B()

            return B(
                message.instance(1, "a", A),
                message.string(2, "s")
            )
        }
    }
}