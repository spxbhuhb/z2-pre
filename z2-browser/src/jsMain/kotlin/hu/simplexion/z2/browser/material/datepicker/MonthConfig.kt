package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

class MonthConfig(
    val year : Int,
    val month: Month,
    val today: LocalDate = hereAndNow().date,
    val firstWeekDay : DayOfWeek = DayOfWeek.MONDAY,
    val markedDays : List<LocalDate> = emptyList()
)