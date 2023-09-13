package hu.simplexion.z2.browser.demo.calendar

import hu.simplexion.z2.browser.components.calendar.year
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import kotlinx.datetime.DayOfWeek

fun Z2.calendarDemo() =
    surfaceContainerLow {
        year(2023, DayOfWeek.MONDAY)
    }