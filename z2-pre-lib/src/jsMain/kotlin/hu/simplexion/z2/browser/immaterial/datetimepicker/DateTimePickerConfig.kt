package hu.simplexion.z2.browser.immaterial.datetimepicker

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.material.textfield.FieldConfig.Companion.defaultFieldStyle
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.localization.text.dateTimeStrings
import kotlinx.datetime.LocalDateTime

class DateTimePickerConfig(
    val style: FieldStyle = defaultFieldStyle
) {

    var dateSupportText: LocalizedText? = dateTimeStrings.localDateSupportText

    var timeSupportText: LocalizedText? = dateTimeStrings.localTimeSupportText

    var leadingIcon: LocalizedIcon? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var trailingIcon: LocalizedIcon? = browserIcons.schedule
        set(value) {
            field = value
            update?.invoke()
        }

    var errorIcon: LocalizedIcon = browserIcons.error
        set(value) {
            field = value
            update?.invoke()
        }

    var onChange: (DockedDateTimePicker.(value: LocalDateTime) -> Unit)? = null
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

    var update: (() -> Unit)? = null

}