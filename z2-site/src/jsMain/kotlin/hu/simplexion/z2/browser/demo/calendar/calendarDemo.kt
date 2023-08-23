package hu.simplexion.z2.browser.demo.calendar

import hu.simplexion.z2.browser.calendar.year
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.layout.low
import kotlinx.datetime.DayOfWeek

fun Z2.calendarDemo() =
    low {
        year(2023, DayOfWeek.MONDAY)
    }