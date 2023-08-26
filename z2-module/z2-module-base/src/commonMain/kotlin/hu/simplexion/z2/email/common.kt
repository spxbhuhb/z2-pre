package hu.simplexion.z2.email

import hu.simplexion.z2.commons.i18n.iconStoreRegistry
import hu.simplexion.z2.commons.i18n.textStoreRegistry
import hu.simplexion.z2.email.ui.emailIcons
import hu.simplexion.z2.email.ui.emailStrings

fun emailCommon() {
    textStoreRegistry += emailStrings
    iconStoreRegistry += emailIcons
}