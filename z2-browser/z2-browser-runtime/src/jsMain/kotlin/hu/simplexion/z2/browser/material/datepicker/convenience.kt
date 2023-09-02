package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.pb16
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

fun Z2.datePicker(
    value : LocalDate = hereAndNow().date,
    label : LocalizedText? = null,
    supportingText: LocalizedText? = commonStrings.localDateSupportText,
    onChange: (value: LocalDate) -> Unit
) =
    DockedDatePicker(this, value, label, supportingText, onChange = onChange)

fun Z2.month(
    year : Int,
    month: Month,
    dense : Boolean = true,
    markedDays : List<LocalDate> = emptyList(),
    dayLetterPadding : String = pb16,
    onSelected: (date : LocalDate) -> Unit
) =
    MonthBase(this, year, month, dense = dense, dayLetterPadding = dayLetterPadding, markedDays = markedDays, onSelected = onSelected)