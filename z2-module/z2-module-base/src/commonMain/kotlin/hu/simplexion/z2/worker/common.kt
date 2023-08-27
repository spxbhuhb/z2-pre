package hu.simplexion.z2.worker

import hu.simplexion.z2.commons.i18n.iconStoreRegistry
import hu.simplexion.z2.commons.i18n.textStoreRegistry
import hu.simplexion.z2.worker.ui.workerIcons
import hu.simplexion.z2.worker.ui.workerStrings

fun workerCommon() {
    textStoreRegistry += workerStrings
    iconStoreRegistry += workerIcons
}