package hu.simplexion.z2.i18n

import hu.simplexion.z2.commons.i18n.iconStoreRegistry
import hu.simplexion.z2.commons.i18n.textStoreRegistry
import hu.simplexion.z2.history.ui.icons
import hu.simplexion.z2.history.ui.strings

fun i18nCommon() {
    textStoreRegistry += strings
    iconStoreRegistry += icons
}