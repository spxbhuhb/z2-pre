package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.api.LocaleApi
import hu.simplexion.z2.localization.api.TranslationApi
import hu.simplexion.z2.localization.locales.setLocalizedFormats
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.localization.text.StaticText
import hu.simplexion.z2.service.getService
import kotlinx.browser.window

val localeService = getService<LocaleApi>()
val translationService = getService<TranslationApi>()

lateinit var effectiveLocale: Locale

suspend fun localeJs() {
    localizationCommon()

    effectiveLocale = localeService.getLocale(window.navigator.language)

    for (translation in translationService.list(effectiveLocale.uuid)) {
        val text = localizedTextStore[translation.key]
        if (text != null) {
            text.value = translation.value
        } else {
            localizedTextStore[translation.key] = StaticText(translation.key, translation.value)
        }
    }

    setLocalizedFormats(effectiveLocale.isoCode)
}