package hu.simplexion.z2.content

import hu.simplexion.z2.commons.i18n.iconStoreRegistry
import hu.simplexion.z2.commons.i18n.textStoreRegistry
import hu.simplexion.z2.content.ui.contentIcons
import hu.simplexion.z2.content.ui.contentStrings

fun contentCommon() {
    textStoreRegistry += contentStrings
    iconStoreRegistry += contentIcons
}