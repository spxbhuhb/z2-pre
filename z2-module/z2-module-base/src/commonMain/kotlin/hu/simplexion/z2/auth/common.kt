package hu.simplexion.z2.auth

import hu.simplexion.z2.auth.ui.authIcons
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.commons.i18n.iconStoreRegistry
import hu.simplexion.z2.commons.i18n.textStoreRegistry

fun authCommon() {
    textStoreRegistry += authStrings
    iconStoreRegistry += authIcons
}