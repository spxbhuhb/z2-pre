package hu.simplexion.z2.browser.schematic

import hu.simplexion.z2.browser.css.displayContents
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.TextField
import hu.simplexion.z2.commons.event.AnonymousEventListener
import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.event.Z2Event
import hu.simplexion.z2.schematic.runtime.SchematicEvent
import hu.simplexion.z2.schematic.runtime.SchematicNode
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

class BoundTextField(
    parent: Z2? = null,
    context: SchematicAccessContext,
    buildFun: Z2.() -> TextField
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    arrayOf(displayContents)
) {
    val schematic = context.schematic
    val field = context.field
    lateinit var textField: TextField

    init {
        attach(schematic) {
            val value = field.getValue(schematic).toString()
            textField.value = value
            textField.error = ! (it.validationResult.fieldResults[field.name]?.valid ?: true)
        }
        textField = buildFun()
        textField.value = field.getValue(schematic).toString()
    }
}