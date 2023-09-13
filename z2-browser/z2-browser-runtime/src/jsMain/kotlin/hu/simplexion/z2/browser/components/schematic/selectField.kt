package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.util.label
import hu.simplexion.z2.schematic.runtime.SchematicAccessFunction
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext

/**
 * An input for the schematic field accessed by [accessor]. The actual input depends on
 * the type of the field.
 */
@Suppress("UNCHECKED_CAST")
@SchematicAccessFunction
fun <T> Z2.radioField(entries : List<T>, context: SchematicAccessContext? = null, @Suppress("UNUSED_PARAMETER") accessor: () -> T): BoundSelectField<T> {
    checkNotNull(context)

    val field = context.field
    val label = field.label()

    return BoundSelectField(this, context) {
        val value = field.getValue(context.schematic)
        radioButtonGroup(value, entries) {
            context.schematic.schematicChange(context.field, it)
        }
    } as BoundSelectField<T>

}



