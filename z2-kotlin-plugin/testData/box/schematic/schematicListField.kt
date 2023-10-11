package foo.bar

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicCompanion

class Test : Schematic<Test>() {
    var intField by int()
    var schematicListField by schematicList<Test>()
}

fun box(): String {
    val test = Test()
    val test2 = Test().also { it.intField = 12 }

    test.schematicListField += test2

    if (test.schematicListField.first().intField != 12) return "Fail"

    return "OK"
}