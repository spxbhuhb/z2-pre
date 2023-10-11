package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.api.LocaleApi
import hu.simplexion.z2.service.getService

val localeService = getService<LocaleApi>()

fun localeJs() {
    localizationCommon()
}