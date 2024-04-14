package hu.simplexion.z2.serialization

data class A(
    var b: Boolean = false,
    var i: Int = 0,
    var s: String = "",
    var l: MutableList<Int> = mutableListOf()
) {
    companion object : InstanceEncoder<A>, InstanceDecoder<A> {

        override fun encodeInstance(builder: MessageBuilder, value: A): ByteArray =
            builder
                .boolean(1, "b", value.b)
                .int(2, "i", value.i)
                .string(3, "s", value.s)
                .intList(4, "l", value.l)
                .pack()

        override fun decodeInstance(message: Message?): A {
            if (message == null) return A()

            return A(
                message.boolean(1, "b"),
                message.int(2, "i"),
                message.string(3, "s"),
                message.intList(4, "l").toMutableList()
            )
        }
    }
}