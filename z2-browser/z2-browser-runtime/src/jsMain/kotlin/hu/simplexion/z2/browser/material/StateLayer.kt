package hu.simplexion.z2.browser.material

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent

class StateLayer(
    parent : Z2,
    classes : Array<out String>
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    classes
) {
    var hasHover : Boolean = false
    var hasFocus : Boolean = false

    init {
        addClass(positionAbsolute)
        addEventListeners()
    }

    fun addEventListeners() {
        onMouseEnter {
            hasHover = true
            updateState()
        }
        onMouseLeave {
            hasHover = false
            updateState()
        }

        parent!!.onFocus {
            hasFocus = true
            updateState()
        }
        parent.onBlur {
            hasFocus = false
            updateState()
        }

        parent.onMouseDown {
            ripple(it)
        }
    }

    fun updateState() {
        removeClass(displayNone, stateLayerOpacityHover, stateLayerOpacityFocus)

       when {
            hasFocus -> addClass(stateLayerOpacityFocus)
            hasHover -> addClass(stateLayerOpacityHover)
            else -> addClass(displayNone)
        }
    }

    /**
     * credit: https://www.geeksforgeeks.org/how-to-create-a-ripple-effect-on-click-the-button/
     */
    fun ripple(event : MouseEvent) {
        val target = (event.target as HTMLElement)
        val x = event.clientX - target.offsetLeft
        val y = event.clientY - target.offsetTop

        val ripple = document.createElement("span") as HTMLElement
        ripple.addClass("ripple")
        ripple.style.left = "${x}px"
        ripple.style.top = "${y}px"

        htmlElement.appendChild(ripple)

        window.setTimeout({ ripple.remove() }, 300)
    }
}