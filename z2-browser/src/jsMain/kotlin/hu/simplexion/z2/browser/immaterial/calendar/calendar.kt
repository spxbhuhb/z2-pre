package hu.simplexion.z2.browser.immaterial.calendar

import hu.simplexion.z2.browser.css.p8
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.datepicker.month
import hu.simplexion.z2.localization.text.monthNameTable
import kotlinx.datetime.Month

fun Z2.year(year: Int) =
    div("calendar-year") {
        for (month in Month.values()) {
            div("year-month") {

                div("month-name", "title-small") {
                    text { monthNameTable[month.ordinal] }
                }

                month(year, month, dayLetterPadding = p8) {  }
            }
        }
    }
