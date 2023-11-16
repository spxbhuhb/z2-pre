package hu.simplexion.z2.browser.material.navigation

import hu.simplexion.z2.browser.css.css
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.onClick
import hu.simplexion.z2.browser.material.icon.icon
import org.w3c.dom.events.Event

fun Z2.navigationRail(builder : Z2.() -> Z2): Z2 =
    div("navigation-rail-container".css) {
        builder()
    }

fun Z2.railItem(item: NavigationItem, onClick: (event : Event) -> Unit): Z2 =

    div("navigation-rail-item".css) {

        if (item.label != null) {
            div("navigation-rail-active-indicator-with-text".css) {
                item.icon?.let { icon(it, cssClass = "navigation-rail-icon-active".css) }
            }
            div("navigation-rail-label-text-active".css, "label-medium".css) {
                text { item.label }
            }
        } else {
            div("navigation-rail-active-indicator-with-no-text".css) {
                item.icon?.let { icon(it, cssClass = "navigation-rail-icon-active".css) }
            }
        }

        this.onClick(onClick)
    }