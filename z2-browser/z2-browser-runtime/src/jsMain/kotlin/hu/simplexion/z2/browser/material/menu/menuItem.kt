package hu.simplexion.z2.browser.material.menu

import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText
import org.w3c.dom.events.Event

fun Z2.menuItem(
    index: Int,
    icon: LocalizedIcon? = null,
    label: LocalizedText,
    trailing: (Z2.() -> Unit)? = null,
    onClick: (event: Event) -> Unit
) {
    val row = index.toString()

    div("menu-item") {
        gridRow = row
        gridColumn = "1"
        if (icon != null) icon(icon, cssClass = "menu-icon")
        this.onClick(onClick)
    }

    div("menu-item", "label-large", "justify-self-start") {
        gridRow = row
        gridColumn = "2"
        div {
            text { label }
        }
        this.onClick(onClick)
    }

    div("menu-item", "menu-trailing") {
        gridRow = row
        gridColumn = "3"
        if (trailing != null) trailing()
        this.onClick(onClick)
    }

    div("menu-row") {
        gridRow = row
        gridColumn = "1/4"
        this.onClick(onClick)
    }
}
