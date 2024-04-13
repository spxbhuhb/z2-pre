package hu.simplexion.z2.adaptive.field.select.demo

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.adaptive.field.FieldValue
import hu.simplexion.z2.adaptive.field.select.SelectField
import hu.simplexion.z2.adaptive.field.select.impl.DropdownListImpl
import hu.simplexion.z2.adaptive.field.select.impl.selectField
import hu.simplexion.z2.adaptive.field.text.impl.OutlinedTextImpl
import hu.simplexion.z2.browser.css.gridGap16
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.html.gridAutoRows
import hu.simplexion.z2.browser.immaterial.schematic.field

fun Z2.selectFieldPlayground() {

    val select = SelectField<Int, Data> {
        fieldConfig.impl = DropdownListImpl.uuid
        selectState.options = listOf(Data(12, "abc"), Data(34, "cde"), Data(56, "efg"))
        selectConfig.optionToValue = { _, option -> option.id }
        selectConfig.valueToString = { field, value -> field.selectState.options.firstOrNull { it.id == value }?.name ?: "" }
        selectConfig.optionToString = { _, option -> option.name }
        selectConfig.valueImpl = OutlinedTextImpl.uuid
    }

    grid("400px 400px", "1fr", gridGap24) {

        val container = div {
            selectField(select)
        }

        grid("1fr", null, gridGap16) {
            gridAutoRows = "min-content"

            Value(this, select.fieldValue, select.fieldState)

            field { select.fieldState.error }
            field { select.fieldState.errorText }
            field { select.fieldState.hasFocus }
            field { select.fieldState.invalidInput }
            field { select.fieldState.touched }

            field { select.fieldConfig.label }
            field { select.fieldConfig.supportEnabled }
            field { select.fieldConfig.supportText }

        }
    }
}

private class Data(
    val id: Int,
    val name: String
)

private class Value(parent: Z2, val fieldValue: FieldValue<Int>, fieldState: FieldState) : Z2(parent) {
    init {
        attach(fieldValue)
        text { "Value: >${fieldValue.valueOrNull}<" }
    }

    override fun onSchematicEvent(event: Z2Event) {
        clear()
        text { "Value: >${fieldValue.valueOrNull}<" }
    }
}