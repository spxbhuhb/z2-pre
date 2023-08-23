package foo.bar

import hu.simplexion.z2.schematic.runtime.schema.Schema
import hu.simplexion.z2.schematic.runtime.schema.field.IntSchemaField
import hu.simplexion.z2.schematic.runtime.Schematic

class Test : Schematic<Test>() {

    var intField by int(min = 5)

}

fun box(): String {
    val test = Test()

    val schema = test.schematicSchema
    val field = schema.fields[0]
    if (field !is IntSchemaField) return "Fail: not IntSchemaField"

    if (field.name != "intField") return "Fail: wrong field name"
    if (field.definitionDefault != null) return "Fail: wrong field default"
    if (field.min != 5) return "Fail: wrong field min"
    if (field.max != null) return "Fail: wrong field max"

    test.intField = 11
    if (test.schematicValues["intField"] != 11) return "Fail: not in schematicValues"

    return if (test.intField == 11) "OK" else "Fail: wrong value by field access"
    return "OK"
}