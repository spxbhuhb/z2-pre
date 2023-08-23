package hu.simplexion.z2.browser.material.navigation

import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

open class NavigationItem(
    val icon: LocalizedIcon? = null,
    val label: LocalizedText? = null,
    val supportText : LocalizedText? = null,
    val smallBadge: Boolean = false,
    val largeBadge: Boolean = false,
    val badgeLabel: String? = null,
    var active: Boolean = true,
    val onClick: ((item : NavigationItem) -> Unit)? = null
)