package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.displayNone
import hu.simplexion.z2.browser.css.positionAbsolute
import hu.simplexion.z2.browser.css.positionRelative
import hu.simplexion.z2.browser.css.w304
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.browser.material.textfield.TextField
import hu.simplexion.z2.util.hereAndNow
import hu.simplexion.z2.localization.locales.localized
import kotlinx.datetime.LocalDate

class DockedDatePicker(
    parent: Z2? = null,
    override val state: FieldState = FieldState(),
    val config: DatePickerConfig = DatePickerConfig()
) : Z2(parent), ValueField<LocalDate> {

    override var value: LocalDate
        get() = checkNotNull(valueOrNull)
        set(value) {
            valueOrNull = value
            textField.value = value.localized
        }

    override var valueOrNull: LocalDate? = hereAndNow().date
        set(value) {
            field = value
            textField.value = value?.localized ?: ""
        }

    val textField: TextField = TextField(this, state, FieldConfig(decodeFromString = { it }).also {
        it.trailingIcon = config.trailingIcon
    }).main()

    val selector = div(positionRelative, displayNone) { zIndex = 100 }

    override fun main(): DockedDatePicker {
        addCss(w304, positionRelative)

        textField.input.addCss(w304)

        textField.input.onFocus {
            if (state.readOnly || state.disabled) return@onFocus
            selector.buildContent()
            selector.removeCss(displayNone)
        }

        textField.input.onBlur {
            selector.addCss(displayNone)
        }

        state.update = { textField.update() }

        return this
    }

    fun close() {
        textField.input.htmlElement.blur()
    }

    fun Z2.buildContent() {
        clear()
        div(positionAbsolute) {
            DockedDatePickerSelector(this, valueOrNull ?: hereAndNow().date) {
                this@DockedDatePicker.value = it
                textField.value = it.localized
                config.onChange?.invoke(this@DockedDatePicker, value)
                close()
            }
        }.onMouseDown {
            it.preventDefault()
        }
    }

}