package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.displayNone
import hu.simplexion.z2.browser.css.positionAbsolute
import hu.simplexion.z2.browser.css.positionRelative
import hu.simplexion.z2.browser.css.wFull
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.ComponentState
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.textfield.outlinedTextField
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.i18n.locales.localized
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.browser.document
import kotlinx.datetime.LocalDate
import org.w3c.dom.HTMLElement

class DockedDatePicker(
    parent: Z2? = null,
    var value: LocalDate = hereAndNow().date,
    label: LocalizedText? = null,
    supportingText: LocalizedText? = commonStrings.localDateSupportText,
    leadingIcon: LocalizedIcon? = null,
    state: ComponentState = ComponentState.Enabled,
    error: Boolean = false,
    onChange: (value: LocalDate) -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes = arrayOf(wFull, positionRelative)
) {

    val textField = outlinedTextField(value.localized, label, supportingText, leadingIcon, basicIcons.calendar)

    val selector = div(positionRelative, displayNone) {  }

    init {
        style.width = 304.px

        with(selector) {
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

        textField.input.onFocus {
            selector.removeClass(displayNone)
        }

        textField.input.onBlur {
            selector.addClass(displayNone)
        }

    }

    fun close() {
        textField.input.htmlElement.blur()
    }
}