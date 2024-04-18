package foo.bar

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicAccessFunction
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.access.SchematicAccessor
import hu.simplexion.z2.schematic.schema.SchemaFieldType

class Test : Schematic<Test>() {
    var intField by int(default = 5)
}

@SchematicAccessFunction
fun testFun(context: SchematicAccessContext? = null, accessor: SchematicAccessor): SchematicAccessContext {
    checkNotNull(context)
    return context
}

fun box(): String {
    val test = Test()
    val context = testFun { test.intField }

    if (context.schematic != test) return "Fail: invalid schematic"
    if (context.field.name != "intField") return "Fail: invalid field name"
    if (context.field.type != SchemaFieldType.Int) return "Fail: invalid field type"
    if (context.value != 5) return "Fail: invalid value"

    return "OK"
}