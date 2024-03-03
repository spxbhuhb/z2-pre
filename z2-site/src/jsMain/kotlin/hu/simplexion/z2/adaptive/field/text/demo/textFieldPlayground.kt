package hu.simplexion.z2.adaptive.field.text.demo

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.FieldConfig
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.adaptive.field.text.TextConfig
import hu.simplexion.z2.adaptive.field.text.TextField
import hu.simplexion.z2.adaptive.field.text.render.BorderBottomRenderer
import hu.simplexion.z2.adaptive.field.text.render.ChipRenderer
import hu.simplexion.z2.adaptive.field.text.render.FilledRenderer
import hu.simplexion.z2.adaptive.field.text.render.OutlinedRenderer
import hu.simplexion.z2.browser.css.gridGap16
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.html.gridAutoRows
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup

fun Z2.textFieldPlayground() {
    val fieldState = FieldState<String>()
    val fieldConfig = FieldConfig()
    val textConfig = TextConfig().also { it.renderer = OutlinedRenderer() }

    val renderers = listOf("OutlinedRenderer", "FilledRenderer", "BorderBottomRenderer", "ChipRenderer")

    grid("400px 400px", "1fr", gridGap24) {

        val container = div()
        TextField(container, fieldState, fieldConfig, textConfig).main()

        grid("1fr", null, gridGap16) {
            gridAutoRows = "min-content"

            radioButtonGroup(
                renderers.first(),
                renderers,
                { text { it } }
            ) {
                container.clear()
                textConfig.renderer = when (it) {
                    "OutlinedRenderer" -> OutlinedRenderer()
                    "FilledRenderer" -> FilledRenderer()
                    "BorderBottomRenderer" -> BorderBottomRenderer()
                    "ChipRenderer" -> ChipRenderer()
                    else -> throw IllegalStateException()
                }
                TextField(container, fieldState, fieldConfig, textConfig).main()
            }

            Value(this, fieldState)

            field { fieldState.error }
            field { fieldState.errorText }
            field { fieldState.hasFocus }
            field { fieldState.invalidInput }
            field { fieldState.touched }

            field { fieldConfig.label }
            field { fieldConfig.supportEnabled }
            field { fieldConfig.supportText }

        }
    }
}

private class Value(parent : Z2, val fieldState: FieldState<String>) : Z2(parent){
    init {
        attach(fieldState)
        text { "Value: >${fieldState.value}<" }
    }

    override fun onSchematicEvent(event: Z2Event) {
        clear()
        text { "Value: >${fieldState.value}<" }
    }
}