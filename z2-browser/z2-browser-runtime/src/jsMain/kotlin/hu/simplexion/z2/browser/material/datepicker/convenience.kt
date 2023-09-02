package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.pb16
import hu.simplexion.z2.browser.html.Z2
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

fun Z2.month(year : Int, month: Month, dense : Boolean = true, dayLetterPadding : String = pb16, onSelected: (date : LocalDate) -> Unit) =
    MonthBase(this, year, month, dense = dense, dayLetterPadding = dayLetterPadding, onSelected = onSelected)