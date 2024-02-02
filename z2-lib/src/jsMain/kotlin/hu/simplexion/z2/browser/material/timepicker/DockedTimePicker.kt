package hu.simplexion.z2.browser.material.timepicker

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.browser.material.textfield.TextField
import hu.simplexion.z2.util.hereAndNow
import kotlinx.datetime.LocalTime

class DockedTimePicker(
    parent: Z2? = null,
    override val state: FieldState = FieldState(),
    val config: TimePickerConfig = TimePickerConfig()
) : Z2(parent), ValueField<LocalTime> {

    override var value: LocalTime
        get() = checkNotNull(valueOrNull)
        set(value) {
            valueOrNull = value
        }

    override var valueOrNull: LocalTime? = hereAndNow().time
        set(value) {
            field = value
            textField.value = if (value != null) {
                "${value.hour}:${value.minute.toString().padStart(2, '0')}"
            } else {
                ""
            }
        }

    val textField: TextField = TextField(this, state, FieldConfig(decodeFromString = { it }).also {
        it.leadingIcon = config.leadingIcon
        it.trailingIcon = config.trailingIcon
    }).main()

    val selector = div(positionRelative, displayNone) { zIndex = 100 }

    override fun main(): DockedTimePicker {

        addCss(minWidth140, positionRelative)

        textField.input.onFocus {
            selector.buildContent()
            selector.removeCss(displayNone)
        }

        textField.input.onBlur {
            selector.addCss(displayNone)
        }

        return this
    }

    fun close() {
        textField.input.htmlElement.blur()
    }

    fun Z2.buildContent() {
        clear()
        div(positionAbsolute, w304) {
//            DockedDatePickerSelector(this, value, { close() }) {
//                this@DockedTimePicker.value = it
//                textField.value = it.localized
//                config.onChange?.invoke(this@DockedTimePicker, value)
//            }
        }.onMouseDown {
            it.preventDefault()
        }
    }

}