package hu.simplexion.z2.browser.material.navigation

import hu.simplexion.z2.browser.css.alignSelfCenter
import hu.simplexion.z2.browser.css.css
import hu.simplexion.z2.browser.css.labelLarge
import hu.simplexion.z2.browser.css.whiteSpaceNoWrap
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.routing.RoutingTarget
import hu.simplexion.z2.localization.locales.localeCapitalized

fun Z2.navigationDrawer(vararg targets : RoutingTarget<Z2>, scrollAutoHide : Boolean = true) =
    navigationDrawer(targets.toList(), scrollAutoHide)

fun Z2.navigationDrawer(targets: Collection<RoutingTarget<Z2>>, scrollAutoHide : Boolean = true) =
    div("navigation-drawer-container".css) {
        if (scrollAutoHide) addClass("scroll-auto-hide")
        for (target in targets) {
            if (!target.visible() || (target.icon == null && target.label == null)) continue
            drawerItem(target)
        }
    }

fun Z2.navigationDrawer(scrollAutoHide : Boolean = true, builder: Z2.() -> Unit) =
    div("navigation-drawer-container".css) {
        if (scrollAutoHide) addCss("scroll-auto-hide".css)
        builder()
    }

fun Z2.drawerItem(target : RoutingTarget<Z2>) {
    drawerItem(NavigationItem(target.icon, target.label) { target.open() })
}

fun Z2.drawerItem(item: NavigationItem, onClick: (() -> Unit)? = null) =

    div("navigation-drawer-item".css) {
        gridTemplateColumns = if (item.badgeLabel != null) {
            "min-content 1fr 24px"
        } else {
            "min-content 1fr"
        }

        item.icon?.let {
            div("navigation-drawer-icon".css) {
                icon(it, weight = 300, cssClass = "navigation-rail-icon".css)
            }
        }

        div("navigation-drawer-label".css, labelLarge, alignSelfCenter, whiteSpaceNoWrap) {
            text { item.label?.localeCapitalized }
            if (item.icon == null) gridColumn = "1/span2"
        }

        if (item.badgeLabel != null) {
            div("navigation-drawer-badge".css, alignSelfCenter) {
                text { item.badgeLabel }
            }
        }

        this.onClick {
            if (onClick != null) onClick()
            item.onClick?.invoke(item)
        }
    }