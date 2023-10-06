package hu.simplexion.z2.browser.immaterial.table

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.builders.TableBuilder
import hu.simplexion.z2.browser.util.label
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicAccessFunction
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext

fun <T> Z2.table(
    builder: TableBuilder<T>.() -> Unit
) = Table(this, builder)


/**
 * An input for the schematic field accessed by [accessor]. The actual input depends on
 * the type of the field.
 */
@SchematicAccessFunction
fun <T : Schematic<T>> TableBuilder<T>.schematicColumn(context: SchematicAccessContext? = null, @Suppress("UNUSED_PARAMETER") accessor: () -> Any?) {
    checkNotNull(context)

    val field = context.field

    column {
        label = field.label(context.schematic)
        render = { schematic -> text { schematic.schematicValues[field.name] } }
        // FIXME schematicColum comparator and field getter
    }
}