package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.css.displayContents
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.TextField
import hu.simplexion.z2.browser.util.io
import hu.simplexion.z2.commons.event.AnonymousEventListener
import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.event.Z2Event
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicEvent
import hu.simplexion.z2.schematic.runtime.SchematicNode
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

class BoundTextField<T>(
    parent: Z2? = null,
    context: SchematicAccessContext,
    buildFun: Z2.() -> TextField
) : BoundField<T>, Z2(
    parent,
    document.createElement("div") as HTMLElement,
    arrayOf(displayContents)
) {
    override val schematic = context.schematic
    @Suppress("UNCHECKED_CAST")
    override val field = context.field as SchemaField<T>

    override var fullSuspendValidation : FullSuspendValidation<T>? = null

    lateinit var textField: TextField

    init {
        attach(schematic) {

            val value = field.getValue(schematic)
            textField.value = value.toString()

            val schemaResult = it.validationResult.fieldResults[field.name]
            val valid = schemaResult?.valid ?: true

            if (fullSuspendValidation == null || !valid) {
                textField.setState(!valid, schemaResult?.fails?.firstOrNull()?.message)
                return@attach
            }

            fullSuspendValidation?.let { validation ->
                io {
                    val result = validation(schematic, field, value)
                    // checking for the value so if the user changed it since we won't set it to an old check result
                    if (value == textField.value) textField.setState(!result.valid, result.fails.firstOrNull()?.message)
                }
            }
        }

        textField = buildFun()
        textField.value = field.getValue(schematic).toString()
    }

}