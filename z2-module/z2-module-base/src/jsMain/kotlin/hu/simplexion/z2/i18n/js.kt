package hu.simplexion.z2.i18n

import hu.simplexion.z2.localization.api.LanguageApi
import hu.simplexion.z2.localization.localizationCommon
import hu.simplexion.z2.service.runtime.getService

val languages = getService<LanguageApi>()

fun i18nJs() {
    localizationCommon()
}