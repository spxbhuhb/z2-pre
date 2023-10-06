package hu.simplexion.z2.browser.demo.calendar

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.calendar.year
import hu.simplexion.z2.browser.layout.surfaceContainerLow

fun Z2.calendarDemo() =
    surfaceContainerLow {
        year(2023)
    }