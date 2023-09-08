package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.textfield.TextFieldConfig.Companion.defaultFieldStyle
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import kotlinx.datetime.LocalDate

class DatePickerConfig(
    val style : FieldStyle = defaultFieldStyle
) {

    var leadingIcon: LocalizedIcon? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var trailingIcon: LocalizedIcon? = basicIcons.calendar
        set(value) {
            field = value
            update?.invoke()
        }

    var errorIcon: LocalizedIcon = basicIcons.error
        set(value) {
            field = value
            update?.invoke()
        }

    var onChange: (DockedDatePicker.(value: LocalDate) -> Unit)? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var onEnter: (() -> Unit)? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var onEscape: (() -> Unit)? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var update : (() -> Unit)? = null

}