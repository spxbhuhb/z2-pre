package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.displayNone
import hu.simplexion.z2.browser.css.positionAbsolute
import hu.simplexion.z2.browser.css.positionRelative
import hu.simplexion.z2.browser.css.wFull
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.ComponentState
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.textfield.filledTextField
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.i18n.locales.localized
import kotlinx.browser.document
import kotlinx.datetime.LocalDate
import org.w3c.dom.HTMLElement

class DockedDatePicker(
    parent: Z2? = null,
    value: LocalDate,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = commonStrings.localDateSupportText,
    leadingIcon: LocalizedIcon? = null,
    state: ComponentState = ComponentState.Enabled,
    error: Boolean = false,
    val onChange: (value: LocalDate) -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes = arrayOf(wFull, positionRelative)
) {

    var value: LocalDate = value
        set(value) {
            field = value
            textField.value = value.localized
        }

    // FIXME add an option to date picker to use outlined field
    val textField = filledTextField(value.localized, label, supportingText, leadingIcon, basicIcons.calendar)

    var readOnly: Boolean = false
        set(value) {
            field = value
            textField.readOnly = value
        }

    val selector = div(positionRelative, displayNone) { zIndex = 100 }

    init {
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

    fun setState(error: Boolean, errorSupportingText: String? = null) {
        textField.setState(error, errorSupportingText)
    }

    fun Z2.buildContent() {
        clear()
        div(positionAbsolute) {
            DockedDatePickerSelector(this, value, { close() }) {
                this@DockedDatePicker.value = it
                textField.value = it.localized
                onChange(value)
            }
        }.onMouseDown {
            it.preventDefault()
        }
    }

}