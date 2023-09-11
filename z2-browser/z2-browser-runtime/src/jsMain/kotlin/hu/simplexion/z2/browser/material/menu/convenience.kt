package hu.simplexion.z2.browser.material.menu

import hu.simplexion.z2.browser.css.borderBottomOutlineVariant
import hu.simplexion.z2.browser.css.boxSizingBorderBox
import hu.simplexion.z2.browser.css.mb8
import hu.simplexion.z2.browser.css.pt8
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.material.icon.actionIcon
import hu.simplexion.z2.commons.i18n.LocalizedIcon

fun Z2.dropdownMenu(builder: Z2.() -> Unit) =
    DropdownMenu(this, false, builder)

fun Z2.more(icon : LocalizedIcon = browserIcons.more, inline: Boolean = false, builder: Z2.() -> Unit) =
    actionIcon(icon, inline = inline) {  }.also {
        DropdownMenu(it, inline, builder)
    }

fun Z2.menuDivider() =
    div(boxSizingBorderBox, pt8, mb8, borderBottomOutlineVariant) {  }
