package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.pb16
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

fun Z2.datePicker(
    value : LocalDate = hereAndNow().date,
    label : LocalizedText? = null,
    supportText: LocalizedText? = commonStrings.localDateSupportText,
    onChange: DockedDatePicker.(value: LocalDate) -> Unit
) =
    DockedDatePicker(
        this,
        FieldState().also {
            it.label = label.toString()
            it.supportText = supportText.toString()
        },
        DatePickerConfig().also {
            it.onChange = onChange
        }
    ).also {
        it.value = value
    }

fun Z2.month(
    year : Int,
    month: Month,
    dense : Boolean = true,
    markedDays : List<LocalDate> = emptyList(),
    dayLetterPadding : String = pb16,
    onSelected: (date : LocalDate) -> Unit
) =
    MonthBase(this, year, month, dense = dense, dayLetterPadding = dayLetterPadding, markedDays = markedDays, onSelected = onSelected)