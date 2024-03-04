package hu.simplexion.z2.adaptive.field.text

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.AdaptiveField
import hu.simplexion.z2.adaptive.field.FieldConfig
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.adaptive.field.FieldValue
import hu.simplexion.z2.browser.html.Z2

class TextField(
    parent: Z2,
    override val fieldValue: FieldValue<String>,
    override val fieldState: FieldState,
    override val fieldConfig: FieldConfig,
    val textConfig: TextConfig
) : Z2(parent), AdaptiveField<String> {

    override fun main(): TextField {
        attach(fieldValue, fieldState, fieldConfig)
        textConfig.renderer.render(this)
        return this
    }

    override fun onSchematicEvent(event: Z2Event) {
        textConfig.renderer.patch(event)
    }

}