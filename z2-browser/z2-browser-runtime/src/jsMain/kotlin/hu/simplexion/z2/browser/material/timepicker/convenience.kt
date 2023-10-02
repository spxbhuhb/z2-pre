package hu.simplexion.z2.browser.material.timepicker

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.datetime.LocalTime

fun Z2.timePicker(
    value : LocalTime = hereAndNow().time,
    label : LocalizedText? = null,
    supportText: LocalizedText? = commonStrings.localDateSupportText,
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
    }