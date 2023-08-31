package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.icon.actionIcon
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.browser.document
import kotlinx.datetime.LocalDate
import org.w3c.dom.HTMLElement

class DatePicker(
    parent : Z2? = null,
    date : LocalDate = hereAndNow().date,
    val config: MonthConfig = MonthConfig(date.year, date.month),
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    emptyArray()
) {

    var date : LocalDate = date

    init {
        build()
    }

    fun build() {
        addClass(displayGrid, wMinContent, surfaceContainerHigh, elevationLevel3, shapeCornerLarge, pt0, pb12, pl12, pr12)
        style.width = 304.px
        style.height = 400.px
        dayView()
    }

    fun Z2.headerContainer(builder : Z2Builder) =
        grid(justifySelfCenter, wFull, pt20, pb30, boxSizingBorderBox) {
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
        header()
        month(config, dense = false)
        actions()
    }

    fun Z2.header() =
        headerContainer {
            div(displayFlex, alignItemsCenter, pl12) {
                actionIcon(basicIcons.left, basicStrings.previous, inline = true) {  }
                monthMenu()
                actionIcon(basicIcons.right, basicStrings.next, inline = true) {  }
            }
            div(displayFlex, alignItemsCenter, justifySelfEnd, pr24) {
                actionIcon(basicIcons.left, basicStrings.previous, inline = true) {  }
                yearMenu()
                actionIcon(basicIcons.right, basicStrings.next, inline = true) {  }
            }
        }

    fun Z2.actions() =
        div(displayFlex, justifyContentEnd) {
            textButton(basicStrings.cancel) {  }
            textButton(basicStrings.ok) {  }
        }

    fun Z2.monthMenu() {
        div(bodySmall, textTransformCapitalize, displayFlex, pl8, cursorPointer) {
            div(alignSelfCenter) { text { date.month.name.lowercase() } }
            icon(basicIcons.down, size = 20)
            onClick { monthSelectView() }
        }
    }

    fun Z2.yearMenu() {
        div(bodySmall, displayFlex, pl8, cursorPointer) {
            div(alignSelfCenter) { text { date.year } }
            icon(basicIcons.down, size = 20)
            onClick { yearSelectView() }
        }
    }

    // ------------------------------------------------------------------------------
    // Month View
    // ------------------------------------------------------------------------------

    fun monthSelectView() {
        clear()
        div {
            monthHeader()
            text { "months" }
            onClick { dayView() }
        }
    }

    fun Z2.monthHeader() =
        headerContainer {
            div(displayFlex, alignItemsCenter, pl24, pr24) {
                monthMenu()
            }

            div(bodySmall, displayFlex, alignItemsCenter, justifySelfEnd, pl24, pr24) {
                div(onSurfaceText, opacity38) { text { date.year } }
                div(pr20) { }
            }
        }


    // ------------------------------------------------------------------------------
    // Year View
    // ------------------------------------------------------------------------------

    fun yearSelectView() {
        clear()
        div {
            text { "year" }
            onClick { dayView() }
        }
    }

}