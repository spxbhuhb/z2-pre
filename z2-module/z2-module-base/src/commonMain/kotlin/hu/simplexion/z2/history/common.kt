package hu.simplexion.z2.history

import hu.simplexion.z2.commons.i18n.iconStoreRegistry
import hu.simplexion.z2.commons.i18n.textStoreRegistry
import hu.simplexion.z2.history.ui.historyIcons
import hu.simplexion.z2.history.ui.historyStrings

fun historyCommon() {
    textStoreRegistry += historyStrings
    iconStoreRegistry += historyIcons
}