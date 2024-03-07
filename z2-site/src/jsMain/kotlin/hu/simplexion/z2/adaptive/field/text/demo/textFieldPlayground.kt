package hu.simplexion.z2.adaptive.field.text.demo

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.FieldConfig
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.adaptive.field.FieldValue
import hu.simplexion.z2.adaptive.field.isOf
import hu.simplexion.z2.adaptive.field.text.TextField
import hu.simplexion.z2.adaptive.field.text.impl.*
import hu.simplexion.z2.adaptive.impl.AdaptiveImplFactory
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
    val textField = TextField().also { it.fieldConfig.impl = OutlinedTextImpl.uuid }

    val settings = TextFieldSettings()

    grid("400px 400px", "1fr", gridGap24) {

        val container = div {
            textField(textField)
        }

        grid("1fr", null, gridGap16) {
            gridAutoRows = "min-content"

            Support(this, container, textField, settings)

            field { textField.fieldState.error }
            field { textField.fieldState.errorText }
            field { textField.fieldState.hasFocus }
            field { textField.fieldState.invalidInput }
            field { textField.fieldState.touched }

            field { textField.fieldConfig.label }
            field { textField.fieldConfig.supportEnabled }
            field { textField.fieldConfig.supportText }

            field { settings.renderer }
            field { settings.leadingIcon }
            field { settings.trailingIcon }
            field { settings.errorIcon }

        }
    }
}

enum class Renderer(
    val impl: AdaptiveImplFactory
) {
    Outlined(OutlinedTextImpl),
    Filled(FilledTextImpl),
    Border(BorderBottomTextImpl),
    Chip(ChipTextImpl)
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
    val field : TextField,
    val settings: TextFieldSettings
) : Z2(
    parent
) {

    val fieldValue: FieldValue<String> = field.fieldValue
    val fieldState: FieldState = field.fieldState
    val fieldConfig: FieldConfig = field.fieldConfig

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

            val renderer = settings.renderer
            if (renderer.impl.uuid != fieldConfig.impl) {
                fieldConfig.impl = renderer.impl.uuid

                container.clear()
                container.textField(field)
            }
        }
    }
}