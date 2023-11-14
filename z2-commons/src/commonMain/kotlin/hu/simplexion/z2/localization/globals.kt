package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText

var traceLocalization : Boolean = false

var fallbackNamespace = ""

val localizedTextStore = mutableMapOf<String, LocalizedText>()

val localizedIconStore = mutableMapOf<String, LocalizedIcon>()

fun withLocalization(provider : LocalizationProvider, block : () -> Unit) {
    val original = fallbackNamespace
    fallbackNamespace = provider.localizationNamespace
    try {
        block()
    } finally {
        fallbackNamespace = original
    }
}