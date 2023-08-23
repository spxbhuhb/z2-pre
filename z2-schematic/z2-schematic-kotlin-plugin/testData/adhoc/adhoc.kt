package foo.bar

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicCompanion
import hu.simplexion.z2.commons.protobuf.ProtoMessage

//enum class E {
//    V1,
//    V2
//}

class Test : Schematic<Test>() {
//    val enum by enum(E.values())
//    val enumWithDefault by enum(E.values(), default = E.V2)
    var b by boolean()
}

fun box(): String {
    val l = Test()
    l.b = true
    return if (l.b) "OK" else "Fail"
}