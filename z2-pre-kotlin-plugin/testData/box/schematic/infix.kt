package foo.bar

import hu.simplexion.z2.schematic.schema.Schema
import hu.simplexion.z2.schematic.schema.field.StringSchemaField
import hu.simplexion.z2.schematic.Schematic

class Test : Schematic<Test>() {

    var stringField by string() minLength 5 maxLength 10

}

fun box(): String {
    val test = Test()

    val schema = test.schematicSchema
    val field = schema.fields[0]
    if (field !is StringSchemaField) return "Fail: not StringSchemaField"

    if (field.name != "stringField") return "Fail: wrong field name"
    if (field.definitionDefault != null) return "Fail: wrong field default"
    if (field.minLength != 5) return "Fail: wrong field min"
    if (field.maxLength != 10) return "Fail: wrong field max"

    test.stringField = "abc"
    if (test.schematicValues["stringField"] != "abc") return "Fail: not in schematicValues"

    return if (test.stringField == "abc") "OK" else "Fail: wrong value by field access"
}