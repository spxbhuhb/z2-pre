package hu.simplexion.z2.browser.material

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.commons.browser.CssClass
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent

class StateLayer(
    parent: Z2,
    classes: Array<out CssClass>,
    disabled : Boolean
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes
) {
    var disabled : Boolean = disabled
        set(value) {
            field = value
            updateState()
        }

    var pressCount: Int = 0
    var hasHover: Boolean = false
    var hasFocus: Boolean = false

    init {
        addCss(positionAbsolute, overflowHidden, primary, displayNone, heightFull, wFull)
        addEventListeners()
    }

    fun addEventListeners() {
        parent !!.onMouseEnter {
            hasHover = true
            updateState()
        }

        parent.onMouseLeave {
            hasHover = false
            updateState()
        }

        parent.onFocus {
            hasFocus = true
            updateState()
        }

        parent.onBlur {
            hasFocus = false
            updateState()
        }

        parent.onMouseDown {
            pressCount++
            ripple(it)
        }
    }

    fun updateState() {
        removeCss(displayNone, stateLayerOpacityHover, stateLayerOpacityFocus)

        when {
            disabled -> addCss(displayNone)
            hasFocus -> addCss(stateLayerOpacityFocus)
            hasHover -> addCss(stateLayerOpacityHover)
            else -> addCss(displayNone)
        }
    }

    /**
     * credit: https://www.geeksforgeeks.org/how-to-create-a-ripple-effect-on-click-the-button/
     */
    fun ripple(event: MouseEvent) {
        val boundingRect = parent!!.htmlElement.getBoundingClientRect()
        val x = event.clientX - boundingRect.x
        val y = event.clientY - boundingRect.y

        val ripple = document.createElement("span") as HTMLElement
        ripple.addClass("ripple")
        ripple.style.left = "${x}px"
        ripple.style.top = "${y}px"

        htmlElement.appendChild(ripple)

        window.setTimeout({
            ripple.remove()
            pressCount--
            updateState()
        }, 1000)
    }
}