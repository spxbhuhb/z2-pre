package hu.simplexion.z2.i18n

import hu.simplexion.z2.i18n.api.LanguageApi
import hu.simplexion.z2.service.runtime.getService

val languages = getService<LanguageApi>()

fun i18nJs() {
    i18nCommon()
}