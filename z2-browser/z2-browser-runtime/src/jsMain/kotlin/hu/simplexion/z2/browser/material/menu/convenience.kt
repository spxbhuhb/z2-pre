package hu.simplexion.z2.browser.material.menu

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.icon.actionIcon
import hu.simplexion.z2.commons.i18n.LocalizedIcon

fun Z2.dropdownMenu(builder: Z2.() -> Unit) =
    DropdownMenu(this, false, builder)

fun Z2.more(icon : LocalizedIcon = basicIcons.more, inline: Boolean = false, builder: Z2.() -> Unit) =

    actionIcon(icon, inline = inline) {  }.also {
        DropdownMenu(it, inline, builder)
    }