package hu.simplexion.z2.browser.material.navigation

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.material.icon.icon
import org.w3c.dom.events.Event

fun Z2.navigationRail(builder : Z2.() -> Z2): Z2 =
    div("navigation-rail-container") {
        builder()
    }

fun Z2.railItem(item: NavigationItem, onClick: (event : Event) -> Unit): Z2 =

    div("navigation-rail-item") {

        if (item.label != null) {
            div("navigation-rail-active-indicator-with-text") {
                item.icon?.let { icon(it, cssClass = "navigation-rail-icon-active") }
            }
            div("navigation-rail-label-text-active", "label-medium") {
                text { item.label }
            }
        } else {
            div("navigation-rail-active-indicator-with-no-text") {
                item.icon?.let { icon(it, cssClass = "navigation-rail-icon-active") }
            }
        }

        this.onClick(onClick)
    }