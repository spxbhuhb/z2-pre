package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.displayNone
import hu.simplexion.z2.browser.css.positionAbsolute
import hu.simplexion.z2.browser.css.positionRelative
import hu.simplexion.z2.browser.css.wFull
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.textfield.FilledTextField
import hu.simplexion.z2.browser.material.textfield.TextFieldConfig
import hu.simplexion.z2.commons.i18n.locales.localized
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.datetime.LocalDate

class DockedDatePicker(
    parent: Z2? = null,
    val state: FieldState = FieldState(),
    val config : DatePickerConfig = DatePickerConfig()
) : Z2(parent) {

    var value: LocalDate = hereAndNow().date
        set(value) {
            field = value
            textField.value = value.localized
        }

    val textField : FilledTextField = FilledTextField(this, state, TextFieldConfig().also {
        it.trailingIcon = config.trailingIcon
    })

    val selector = div(positionRelative, displayNone) { zIndex = 100 }

    init {
        addClass(wFull, positionRelative)
        style.width = 304.px

        textField.input.onFocus {
            selector.buildContent()
            selector.removeClass(displayNone)
        }

        textField.input.onBlur {
            selector.addClass(displayNone)
        }
    }

    fun close() {
        textField.input.htmlElement.blur()
    }

    fun Z2.buildContent() {
        clear()
        div(positionAbsolute) {
            DockedDatePickerSelector(this, value, { close() }) {
                this@DockedDatePicker.value = it
                textField.value = it.localized
                config.onChange?.invoke(this@DockedDatePicker, value)
            }
        }.onMouseDown {
            it.preventDefault()
        }
    }

}