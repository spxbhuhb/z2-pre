package hu.simplexion.z2.browser.material.timepicker

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.localization.text.LocalizedText
import hu.simplexion.z2.commons.localization.text.dateTimeStrings
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.datetime.LocalTime

fun Z2.timePicker(
    value : LocalTime = hereAndNow().time,
    label : LocalizedText? = null,
    supportText: LocalizedText? = dateTimeStrings.localTimeSupportText,
    onChange: TimePickerConfig.(value: LocalTime) -> Unit
) =
    DockedTimePicker(
        this,
        FieldState().also {
            it.label = label.toString()
            it.supportText = supportText.toString()
        },
        TimePickerConfig().also {
            it.onChange = onChange
        }
    ).also {
        it.value = value
    }.main()