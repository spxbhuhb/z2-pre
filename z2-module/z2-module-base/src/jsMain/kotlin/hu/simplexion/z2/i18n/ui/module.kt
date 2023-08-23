package hu.simplexion.z2.i18n.ui

import hu.simplexion.z2.i18n.api.LanguageApi
import hu.simplexion.z2.i18n.i18nCommon
import hu.simplexion.z2.service.runtime.getService

val Languages = getService<LanguageApi>()

fun i18n() {
    i18nCommon()
}