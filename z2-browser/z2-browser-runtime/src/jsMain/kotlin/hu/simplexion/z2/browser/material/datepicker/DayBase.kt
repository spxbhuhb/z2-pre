package hu.simplexion.z2.browser.material.datepicker

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.browser.document
import kotlinx.datetime.LocalDate
import org.w3c.dom.HTMLElement

class DayBase(
    parent: Z2? = null,
    val date: LocalDate = hereAndNow().date,
    otherMonth: Boolean,
    dense: Boolean,
    marked: Boolean,
    today: Boolean,
    onSelected: (date: LocalDate) -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes = arrayOf(boxSizingBorderBox, displayFlex, justifyContentCenter, cursorPointer, bodySmall, positionRelative, overflowHidden)
) {
    // TODO think about date picker class list, it is long and when rendering a whole year it is an unnecessary repetition
    val stateLayer = div(displayNone, primary, stateLayerOpacityHover, positionAbsolute, widthFull, heightFull) {  }

    init {
        if (dense) addClass(h32, w32, borderRadius16) else addClass(h40, w40, borderRadius20)

        if (marked) {
            addClass(primary, onPrimaryText)
        } else {
            addClass(onSurfaceText)
            if (otherMonth) addClass(opacity38)
            if (today) addClass(borderPrimary)
        }

        div(alignSelfCenter) { text { date.dayOfMonth } }

        onClick { onSelected(date) }

        onMouseEnter {
            stateLayer.removeClass(displayNone)
        }
        onMouseLeave {
            stateLayer.addClass(displayNone)
        }
    }

}