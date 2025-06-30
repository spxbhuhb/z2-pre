package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.api.LocaleApi
import hu.simplexion.z2.localization.api.TranslationApi
import hu.simplexion.z2.localization.locales.setLocalizedFormats
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.localization.text.StaticText
import hu.simplexion.z2.services.getService
import kotlinx.browser.window

val localeService = getService<LocaleApi>()
val translationService = getService<TranslationApi>()

lateinit var effectiveLocale: Locale

suspend fun localeJs(language: String? = null, set: Boolean = false) {
    localizationCommon()

    var effectiveLanguage = language
    val storedLanguage = window.localStorage.getItem("userLanguage");

    if (effectiveLanguage == null) {
        effectiveLanguage = storedLanguage ?: window.navigator.language
    }

    if (set) {
        window.localStorage.setItem("userLanguage", effectiveLanguage)
    }

    effectiveLocale = localeService.getLocale(effectiveLanguage)

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