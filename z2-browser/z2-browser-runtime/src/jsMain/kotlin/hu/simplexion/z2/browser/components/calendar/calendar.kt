package hu.simplexion.z2.browser.components.calendar

import hu.simplexion.z2.browser.css.p8
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.datepicker.month
import hu.simplexion.z2.commons.i18n.monthNameTable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

fun Z2.year(year: Int, startDay: DayOfWeek) =
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
