package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.alignSelfCenter
import hu.simplexion.z2.browser.css.bodySmall
import hu.simplexion.z2.browser.css.justifySelfCenter
import hu.simplexion.z2.browser.css.pb16
import hu.simplexion.z2.browser.html.*
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

fun Z2.month(config: MonthConfig, dense : Boolean = true, dayLetterPadding : String = pb16) =
    grid {
        gridTemplateColumns = "repeat(7, min-content)"
        gridAutoRows = "min-content"

        var day = LocalDate(config.year, config.month, 1)

        // find the first day to display, this may be in the previous month
        while (day.dayOfWeek != config.firstWeekDay) {
            day = day.minus(1, DateTimeUnit.DAY)
        }

        // header with the day names
        var weekDay = day

        for (i in 1..7) {
            div(alignSelfCenter, justifySelfCenter, bodySmall, "month-day-letter", dayLetterPadding) {
                text { weekDay.dayOfWeek.name.first() }
            }
            weekDay = weekDay.plus(1, DateTimeUnit.DAY)
        }

        // add weeks
        for (i in 1..6) {
            for (j in 1..7) {
                div("month-day", bodySmall, dayClass(day, config), if (dense) "month-day-dense" else "month-day-normal") {
                    if (day in config.markedDays) addClass("marked")
                    text { day.dayOfMonth }
                }
                day = day.plus(1, DateTimeUnit.DAY)
            }
        }
    }

fun dayClass(day: LocalDate, config: MonthConfig): String =
    when {
        day.month == config.month && day == config.today -> "today"
        day.month == config.month -> "this"
        else -> "other"
    }