package hu.simplexion.z2.content

import hu.simplexion.z2.commons.i18n.iconStoreRegistry
import hu.simplexion.z2.commons.i18n.textStoreRegistry
import hu.simplexion.z2.content.ui.icons
import hu.simplexion.z2.content.ui.strings

fun contentCommon() {
    textStoreRegistry += strings
    iconStoreRegistry += icons
}