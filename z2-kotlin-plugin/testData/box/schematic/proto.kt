package foo.bar

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.commons.protobuf.dumpProto
import hu.simplexion.z2.schematic.runtime.Schematic

class Test : Schematic<Test>() {
    var intField by int(default = 5)
}

fun box(): String {
    val test = Test()

    val companion = test.schematicCompanion

    if (companion.schematicSchema !== test.schematicSchema) return "Fail: companion class"

    val wireformat = ProtoMessageBuilder().instance(1, companion, test).pack()
    println(wireformat.dumpProto())

    val test2 = ProtoMessage(wireformat).instance(1, companion)

    return if (test.intField == test2.intField) "OK" else "Fail: intField"
}