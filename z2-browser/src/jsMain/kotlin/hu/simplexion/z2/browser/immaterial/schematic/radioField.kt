package hu.simplexion.z2.browser.immaterial.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.schematic.SchematicAccessFunction
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.dump

/**
 * An input for the schematic field accessed by [accessor]. The actual input depends on
 * the type of the field.
 */
@Suppress("UNCHECKED_CAST")
@SchematicAccessFunction
fun <T> Z2.radioField(context: SchematicAccessContext? = null, @Suppress("UNUSED_PARAMETER") accessor: () -> T): BoundSelectField<T> {
    checkNotNull(context)

    val field = context.field
    val label = field.label(context.schematic)

    return try {

        BoundSelectField(this, context) {
            val value = field.getValue(context.schematic)
            radioButtonGroup(value, emptyList()) {
                context.schematic.schematicChange(context.field, it)
            }
        } as BoundSelectField<T>

    } catch (ex: Exception) {
        println("error in field builder")
        println("schematic: ${context.schematic.dump()}")
        println("field: ${context.field.name}")
        throw ex
    }

}



