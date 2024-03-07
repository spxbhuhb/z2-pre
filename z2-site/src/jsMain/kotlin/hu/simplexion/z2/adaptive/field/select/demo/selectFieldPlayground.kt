package hu.simplexion.z2.adaptive.field.select.demo

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.FieldConfig
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.adaptive.field.FieldValue
import hu.simplexion.z2.adaptive.field.select.SelectConfig
import hu.simplexion.z2.adaptive.field.select.SelectField
import hu.simplexion.z2.adaptive.field.select.SelectState
import hu.simplexion.z2.adaptive.field.select.impl.dropdown.AbstractDropdownListImpl
import hu.simplexion.z2.adaptive.field.text.impl.FilledTextImpl
import hu.simplexion.z2.browser.css.gridGap16
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.html.gridAutoRows
import hu.simplexion.z2.browser.immaterial.schematic.field

fun Z2.selectFieldPlayground() {
    val fieldValue = FieldValue<Int>()
    val fieldState = FieldState()
    val fieldConfig = FieldConfig()
    val selectState = SelectState<Data>().also {
        it.options = listOf(Data(12, "abc"), Data(34, "cde"), Data(56, "efg"))
    }
    val selectConfig = SelectConfig<Int, Data>().apply {
        renderer = AbstractDropdownListImpl()
        optionToValue = { _, option -> option.id }
        valueToString = { field, value -> field.selectState.options.first { it.id == value}.name }
        renderItem = { _, option -> text { option.name } }
        textFieldRenderer = FilledTextImpl()
    }

    grid("400px 400px", "1fr", gridGap24) {

        val container = div()
        SelectField(container, fieldValue, fieldState, fieldConfig, selectState, selectConfig).main()

        grid("1fr", null, gridGap16) {
            gridAutoRows = "min-content"

            Value(this, fieldValue, fieldState)

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

private class Data(
    val id : Int,
    val name : String
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