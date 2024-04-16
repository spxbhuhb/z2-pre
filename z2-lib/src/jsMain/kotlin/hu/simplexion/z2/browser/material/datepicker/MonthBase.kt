package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.deprecated.browser.CssClass
import hu.simplexion.z2.util.hereAndNow
import kotlinx.browser.document
import kotlinx.datetime.*
import org.w3c.dom.HTMLElement

class MonthBase(
    parent: Z2? = null,
    val year : Int,
    val month : Month,
    val today : LocalDate = hereAndNow().date,
    dense : Boolean,
    markedDays : List<LocalDate> = emptyList(),
    dayLetterPadding : CssClass = pb16,
    onSelected: (date : LocalDate) -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes = arrayOf(displayGrid)
) {

    init {
        grid {
            gridTemplateColumns = "repeat(7, min-content)"
            gridAutoRows = "min-content"

            var day = LocalDate(year, month, 1)

            // find the first day to display, this may be in the previous month
            while (day.dayOfWeek != firstDayOfWeek) {
                day = day.minus(1, DateTimeUnit.DAY)
            }

            // header with the day names
            var weekDay = day

            for (i in 1..7) {
                div(alignSelfCenter, justifySelfCenter, bodySmall, dayLetterPadding) {
                    text { weekDay.dayOfWeek.name.first() }
                }
                weekDay = weekDay.plus(1, DateTimeUnit.DAY)
            }

            // add weeks
            for (i in 1..6) {
                for (j in 1..7) {
                    DayBase(this, day, day.month != month, dense, day in markedDays, day == today, onSelected)
                    day = day.plus(1, DateTimeUnit.DAY)
                }
            }
        }
    }
}