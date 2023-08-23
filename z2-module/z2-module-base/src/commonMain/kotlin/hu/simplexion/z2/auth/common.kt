package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.ui.icons
import hu.simplexion.z2.auth.ui.strings
import hu.simplexion.z2.commons.i18n.iconStoreRegistry
import hu.simplexion.z2.commons.i18n.textStoreRegistry

fun authCommon() {
    textStoreRegistry += strings
    iconStoreRegistry += icons
}