package hu.simplexion.z2.browser.form

import hu.simplexion.z2.browser.css.displayContents
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.TextField
import hu.simplexion.z2.commons.event.AnonymousEventListener
import hu.simplexion.z2.commons.event.EventCentral
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

    val listener = AnonymousEventListener {
        val value = field.getValue(schematic).toString()
        textField.value = value
        val vr = schematic.schematicSchema.validate(schematic)
        textField.error = !(vr.fieldResults[field.name]?.valid ?: true)
    }

    init {
        EventCentral.attach(schematic.handle, listener)
        textField = buildFun()
        textField.value = field.getValue(schematic).toString()
    }

    override fun dispose() {
        EventCentral.detach(schematic.handle, listener)
        super.clear()
    }


}