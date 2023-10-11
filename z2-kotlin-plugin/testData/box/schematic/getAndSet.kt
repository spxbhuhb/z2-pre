package foo.bar

import hu.simplexion.z2.schematic.runtime.schema.Schema
import hu.simplexion.z2.schematic.runtime.schema.field.IntSchemaField
import hu.simplexion.z2.schematic.runtime.Schematic

class Test : Schematic<Test>() {
    var intField by int(min = 5)
}

fun box(): String {
    val test = Test()

    val companion = test.schematicCompanion

    companion.setFieldValue(test, "intField", 12)
    if (test.intField != 12) return "Fail: value not set"
    val get = companion.getFieldValue(test, "intField")
    if (get != 12) return "Fail: invalid get value $get"

    return "OK"
}