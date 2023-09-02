package foo.bar

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicCompanion
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import kotlin.time.Duration.Companion.seconds

class Test : Schematic<Test>() {
    var d by duration(1.seconds)
    var retryCheckInterval2 = 300.seconds
}

fun box(): String {
    val t = Test()
    if (t.d != 1.seconds) return "Fail: default"
    t.d = 2.seconds
    if (t.d != 2.seconds) return "Fail: readback"
    return "OK"
}