package hu.simplexion.z2.adaptive.field

import hu.simplexion.z2.adaptive.field.text.impl.textField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.schematic.SchematicAccessFunction
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.dump
import hu.simplexion.z2.schematic.schema.SchemaFieldType

@SchematicAccessFunction
fun <T> Z2.schematicField(context: SchematicAccessContext? = null, @Suppress("UNUSED_PARAMETER") accessor: () -> T) {
    checkNotNull(context)

    val field = context.field

    try {

        when (field.type) {
            SchemaFieldType.String -> textField(context)
            else -> throw NotImplementedError("field type ${field.type} is not implemented yet")
        }

    } catch (ex: Exception) {
        println("error in field builder")
        println("schematic: ${context.schematic.dump()}")
        println("field: ${field.name}")
        throw ex
    }
}