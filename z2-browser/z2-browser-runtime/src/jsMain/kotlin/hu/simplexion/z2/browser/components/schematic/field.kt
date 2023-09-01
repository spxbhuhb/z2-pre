package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.textfield.filledTextField
import hu.simplexion.z2.browser.material.textfield.outlinedTextField
import hu.simplexion.z2.browser.util.label
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicAccessFunction
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import org.w3c.dom.events.Event

/**
 * An input for the schematic field accessed by [accessor]. The actual input depends on
 * the type of the field.
 */
@SchematicAccessFunction
fun Z2.field(context : SchematicAccessContext? = null, @Suppress("UNUSED_PARAMETER") accessor : () -> Any?) {
    checkNotNull(context)

    val field = context.field
    val label = field.label()

    BoundTextField(this, context) {
        filledTextField("", label, label.support) {
            context.schematic.schematicChange(context.field, it)
        }
    }
}

