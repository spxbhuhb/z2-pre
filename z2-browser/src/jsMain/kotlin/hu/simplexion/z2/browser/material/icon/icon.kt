package hu.simplexion.z2.browser.material.icon

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.html.onMouseDown
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText
import org.w3c.dom.events.Event

fun Z2.icon(
    icon: LocalizedIcon,
    size: Int = 24,
    weight : Int = 400,
    fill : Int = 0,
    pointer : Boolean = true,
    cssClass: String? = null
) : Z2 =
    div {
        cssClass?.let { addClass(it) }

        addClass("icon-$size")
        if (pointer) style.cursor = "pointer"

        htmlElement.innerHTML = """<span class="material-symbols-sharp symbols-$weight-$size-$fill">${icon}</span>"""

        onMouseDown { it.preventDefault() }// to avoid focus
    }

fun Z2.actionIcon(
    icon: LocalizedIcon,
    hint: LocalizedText? = null,
    size: Int = 24,
    weight : Int = 400,
    fill : Int = 0,
    cssClass: String? = null,
    inline : Boolean = false,
    onClick: (event : Event) -> Unit
) : Z2 =
    div {
        cssClass?.let { addClass(it) }

        addClass("icon-$size", if (inline) "inline-action-icon" else "action-icon")

        htmlElement.innerHTML = """<span class="material-symbols-sharp symbols-$weight-$size-$fill">${icon}</span>"""

        if (hint != null) {
            addClass("tooltip")
            div("plain-tooltip", "body-small") { text { hint } }
        }

        onMouseDown { it.preventDefault() }// to avoid focus
        this.onClick(onClick)
    }

fun Z2.inlineActionIcon(
    icon: LocalizedIcon,
    hint: LocalizedText? = null,
    size: Int = 24,
    weight : Int = 400,
    fill : Int = 0,
    cssClass: String? = null,
    onClick: (event : Event) -> Unit
) : Z2 =
    actionIcon(icon, hint, size, weight, fill, cssClass, true, onClick)