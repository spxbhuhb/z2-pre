package hu.simplexion.z2.adaptive.field.text.demo

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.*
import hu.simplexion.z2.adaptive.field.text.TextConfig
import hu.simplexion.z2.adaptive.field.text.TextField
import hu.simplexion.z2.adaptive.field.text.render.BorderBottomRenderer
import hu.simplexion.z2.adaptive.field.text.render.ChipRenderer
import hu.simplexion.z2.adaptive.field.text.render.FilledRenderer
import hu.simplexion.z2.adaptive.field.text.render.OutlinedRenderer
import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.gridGap16
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.html.gridAutoRows
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.schematic.Schematic

fun Z2.textFieldPlayground() {
    val fieldValue = FieldValue<String>()
    val fieldState = FieldState()
    val fieldConfig = FieldConfig()
    val textConfig = TextConfig().also { it.renderer = OutlinedRenderer() }
    val settings = TextFieldSettings()

    grid("400px 400px", "1fr", gridGap24) {

        val container = div()
        TextField(container, fieldValue, fieldState, fieldConfig, textConfig).main()

        grid("1fr", null, gridGap16) {
            gridAutoRows = "min-content"

            Support(this, container, fieldValue, fieldState, fieldConfig, textConfig, settings)

            field { fieldState.error }
            field { fieldState.errorText }
            field { fieldState.hasFocus }
            field { fieldState.invalidInput }
            field { fieldState.touched }

            field { fieldConfig.label }
            field { fieldConfig.supportEnabled }
            field { fieldConfig.supportText }

            field { settings.renderer }
            field { settings.leadingIcon }
            field { settings.trailingIcon }
            field { settings.errorIcon }

        }
    }
}

enum class Renderer(
    val factory: () -> FieldRenderer<TextField,String>
) {
    Outlined({ OutlinedRenderer() }),
    Filled({ FilledRenderer() }),
    Border({ BorderBottomRenderer() }),
    Chip({ ChipRenderer() })
}

class TextFieldSettings : Schematic<TextFieldSettings>() {
    val renderer by enum<Renderer>()
    val leadingIcon by boolean()
    val trailingIcon by boolean()
    val errorIcon by boolean()
}


class Support(
    parent: Z2,
    val container : Z2,
    val fieldValue: FieldValue<String>,
    val fieldState: FieldState,
    val fieldConfig: FieldConfig,
    val textConfig: TextConfig,
    val settings: TextFieldSettings
) : Z2(
    parent
) {
    init {
        attach(fieldValue, fieldState, settings)
        text { "Value: >${fieldValue.valueOrNull}<" }
    }

    override fun onSchematicEvent(event: Z2Event) {
        clear()
        text { "Value: >${fieldValue.valueOrNull}<" }

        if (event.isOf(settings)) {
            fieldConfig.leadingIcon = if (settings.leadingIcon) browserIcons.error else null
            fieldConfig.trailingIcon = if (settings.trailingIcon) browserIcons.settings else null
            fieldConfig.errorIcon = if (settings.errorIcon) browserIcons.error else null

            val renderer = settings.renderer.factory()
            if (renderer::class != textConfig.renderer::class) {
                container.clear()
                textConfig.renderer = renderer
                TextField(container, fieldValue, fieldState, fieldConfig, textConfig).main()
            }
        }
    }
}