package hu.simplexion.z2.browser.calendar

import hu.simplexion.z2.browser.css.p8
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.datepicker.MonthConfig
import hu.simplexion.z2.browser.material.datepicker.month
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

fun Z2.year(year: Int, startDay: DayOfWeek) =
    div("calendar-year") {
        for (month in Month.values()) {
            div("year-month") {

                val config = MonthConfig(year, month, hereAndNow().date, startDay, listOf(LocalDate(year, month, 12)))

                div("month-name", "title-small") {
                    text { config.month.name }
                }

                month(config, dayLetterPadding = p8)
            }
        }
    }
