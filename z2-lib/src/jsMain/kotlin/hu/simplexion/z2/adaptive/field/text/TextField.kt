package hu.simplexion.z2.adaptive.field.text

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.FieldConfig
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.browser.html.Z2

class TextField(
    parent: Z2,
    val fieldState: FieldState<String>,
    val fieldConfig: FieldConfig,
    val textConfig: TextConfig
) : Z2(parent) {

    override fun main(): TextField {
        attach(fieldState, fieldConfig, textConfig)
        textConfig.renderer.render(this)
        return this
    }

    override fun onSchematicEvent(event: Z2Event) {
        println("Event: ${event::class}")
        textConfig.renderer.render(this)
    }

}