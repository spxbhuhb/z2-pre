package hu.simplexion.z2.serialization.protobuf

import kotlin.test.Test

class ProtoBuiltinTest : AbstractBuiltinTest() {

    @Test
    fun testBuiltins() {
        testBuiltins(ProtoMessageBuilder()) {
            println(it.dumpProto())
            ProtoMessage(it)
        }
    }

    @Test
    fun standaloneTest() {
        standaloneTest(ProtoStandaloneValue) { ProtoMessage(ProtoMessageBuilder().apply { it() }.pack())}

    }
}