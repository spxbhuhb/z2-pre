package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.icon.actionIcon
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.menu.menuItem
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.commons.i18n.monthNameTable
import hu.simplexion.z2.commons.i18n.monthShortNameTable
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.browser.document
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.w3c.dom.HTMLElement
import kotlin.math.max

class DockedDatePickerSelector(
    parent: Z2? = null,
    date: LocalDate = hereAndNow().date,
    val onClose: () -> Unit = { },
    val onSelected: (date: LocalDate) -> Unit,
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    emptyArray()
) {

    companion object {
        const val DAY_SELECT = 0
        const val MONTH_SELECT = 1
        const val YEAR_SELECT = 2
    }

    var date: LocalDate = date
        set(value) {
            onSelected(value)
            field = value
            dayView()
        }

    init {
        build()
    }

    fun build() {
        addClass(displayGrid, wMinContent, surfaceContainerHigh, elevationLevel3, shapeCornerLarge, p0, pb12, positionRelative)
        style.width = 304.px
        style.height = 400.px
        dayView()
    }

    fun Z2.headerContainer(vararg classes: String, builder: Z2Builder) =
        grid(justifySelfCenter, wFull, pt20, boxSizingBorderBox, pl12, pr12, *classes) {
            gridTemplateColumns = "1fr 1fr"
            gridTemplateRows = "24px"
            builder()
        }

    // ------------------------------------------------------------------------------
    // Day View
    // ------------------------------------------------------------------------------

    fun dayView() {
        clear()
        gridTemplateRows = "min-content min-content min-content"
        gridTemplateColumns = "1fr"
        header(DAY_SELECT)
        div(pl12, pr12) {
            month(date.year, date.month, markedDays = listOf(date), dense = false) { onSelected(it) }
        }
        actions()
    }

    fun Z2.header(mode: Int) =
        headerContainer {
            if (mode == DAY_SELECT) {
                addClass(pb30)
            } else {
                addClass(pb16, borderBottomOutlineVariant)
            }

            div(displayFlex, alignItemsCenter, pl12) {
                left(mode) { date = date.minus(1, DateTimeUnit.MONTH) }
                monthMenu(mode)
                right(mode) { date = date.plus(1, DateTimeUnit.MONTH) }
            }

            div(displayFlex, alignItemsCenter, justifySelfEnd, pr24) {
                left(mode) { date = date.minus(1, DateTimeUnit.YEAR) }
                yearMenu(mode)
                right(mode) { date = date.plus(1, DateTimeUnit.YEAR) }
            }
        }

    fun Z2.left(mode: Int, onClick: () -> Unit) =
        if (mode == DAY_SELECT) {
            actionIcon(basicIcons.left, basicStrings.previous, inline = true) { onClick() }
        } else {
            div(pl24) { }
        }

    fun Z2.right(mode: Int, onClick: () -> Unit) =
        if (mode == DAY_SELECT) {
            actionIcon(basicIcons.right, basicStrings.next, inline = true) { onClick() }
        } else {
            div(pl24) { }
        }

    fun Z2.actions() =
        div(displayFlex, justifyContentEnd, pl12, pr12) {
            textButton(basicStrings.cancel) { onClose() }
            textButton(basicStrings.ok) { onClose() }
        }

    fun Z2.monthMenu(mode: Int) =
        div(bodySmall, textTransformCapitalize, displayFlex, pl8, cursorPointer) {
            div(displayFlex, w60, justifyContentCenter) {
                div(alignSelfCenter, overflowHidden) { text { monthShortNameTable[date.monthNumber - 1] } }
                if (mode != YEAR_SELECT) {
                    icon(basicIcons.down, size = 20)
                } else {
                    div(pl20) { }
                }
            }
            when (mode) {
                DAY_SELECT -> onClick { monthSelectView() }
                MONTH_SELECT -> onClick { dayView() }
                YEAR_SELECT -> addClass(onSurfaceText, opacity38)
            }
        }

    fun Z2.yearMenu(mode: Int) =
        div(bodySmall, displayFlex, pl8, cursorPointer) {
            div(alignSelfCenter) { text { date.year } }
            if (mode != MONTH_SELECT) {
                icon(basicIcons.down, size = 20)
            } else {
                div(pl20) { }
            }
            when (mode) {
                DAY_SELECT -> onClick { yearSelectView() }
                MONTH_SELECT -> addClass(onSurfaceText, opacity38)
                YEAR_SELECT -> onClick { dayView() }
            }
        }

    // ------------------------------------------------------------------------------
    // Month View
    // ------------------------------------------------------------------------------

    fun monthSelectView() {
        clear()
        gridTemplateColumns = "1fr"
        gridTemplateRows = "min-content minmax(0,1fr)"

        header(MONTH_SELECT)

        div(hFull, overflowYAuto) {
            for (monthNumber in 1..12) {
                menuItem(
                    monthNumber,
                    leading = {
                        if (date.monthNumber == monthNumber) {
                            icon(basicIcons.check)
                        } else {
                            div(pr24) {}
                        }
                    },
                    label = monthNameTable[monthNumber - 1]
                ) { monthSelected(monthNumber) }
            }
        }.also {
            it.htmlElement.scrollTop = max((48.0 * (date.monthNumber - 3)), 0.0)
        }
    }

    fun monthSelected(monthNumber: Int) {
        date = LocalDate(date.year, monthNumber, date.dayOfMonth)
        onSelected(date)
        dayView()
    }

    // ------------------------------------------------------------------------------
    // Year View
    // ------------------------------------------------------------------------------

    fun yearSelectView() {
        clear()
        gridTemplateColumns = "1fr"
        gridTemplateRows = "min-content minmax(0,1fr)"

        header(YEAR_SELECT)

        div(hFull, overflowYAuto) {
            for (year in datePickerStartYear..datePickerEndYear) {
                menuItem(
                    year,
                    leading = {
                        if (date.year == year) {
                            icon(basicIcons.check)
                        } else {
                            div(pr24) {}
                        }
                    },
                    label = year.toString()
                ) { yearSelected(year) }
            }
        }.also {
            it.htmlElement.scrollTop = max((48.0 * (date.year - datePickerStartYear - 3)), 0.0)
        }
    }

    fun yearSelected(year: Int) {
        date = LocalDate(year, date.monthNumber, date.dayOfMonth)
        onSelected(date)
        dayView()
    }

}