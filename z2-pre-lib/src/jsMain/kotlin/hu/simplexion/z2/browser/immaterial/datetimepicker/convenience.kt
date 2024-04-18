package hu.simplexion.z2.browser.immaterial.datetimepicker

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.localization.text.dateTimeStrings
import hu.simplexion.z2.util.hereAndNow
import kotlinx.datetime.LocalDateTime

fun Z2.dateTimePicker(
    value: LocalDateTime? = hereAndNow(),
    label: LocalizedText? = null,
    dateSupportText: LocalizedText? = dateTimeStrings.localDateSupportText,
    timeSupportText: LocalizedText? = dateTimeStrings.localTimeSupportText,
    onChange: DockedDateTimePicker.(value: LocalDateTime) -> Unit
) =
    DockedDateTimePicker(
        this,
        FieldState().also {
            it.label = label.toString()
        },
        DateTimePickerConfig().also {
            it.onChange = onChange
            it.dateSupportText = dateSupportText
            it.timeSupportText = timeSupportText
        }
    ).also {
        it.valueOrNull = value
    }