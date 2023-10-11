package hu.simplexion.z2.browser.immaterial.schematic

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.switch.SwitchConfig
import hu.simplexion.z2.browser.material.switch.SwitchField
import hu.simplexion.z2.browser.util.label
import hu.simplexion.z2.schematic.SchematicAccessFunction
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.dump

@Suppress("UNCHECKED_CAST")
@SchematicAccessFunction
fun Z2.switchField(context: SchematicAccessContext? = null, @Suppress("UNUSED_PARAMETER") accessor: () -> Boolean): BoundField<Boolean> {
    checkNotNull(context)

    val field = context.field
    val label = field.label(context.schematic)

    return try {
        BoundField(this, context) {
            SwitchField(
                this,
                FieldState(),
                SwitchConfig().also {
                    it.onChange = { f -> context.schematic.schematicChange(context.field, f.value) }
                }
            ).main().also {
                it.valueOrNull = context.field.getValue(context.schematic) as? Boolean?
            }
        }

    } catch (ex: Exception) {
        println("error in field builder")
        println("schematic: ${context.schematic.dump()}")
        println("field: ${context.field.name}")
        throw ex
    }

}



