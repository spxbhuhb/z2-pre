package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.localization.text.StaticText

fun Array<out Pair<LocalizedText, Any?>>.format(separator: String = ", "): String {
    val formatted = mutableListOf<String>()
    for (item in this) {
        formatted += "${item.first}=${item.second}"
    }
    return formatted.joinToString(separator)
}

fun localizationFor(key : String, fallback : String) : LocalizedText {
    val value = localizedTextStore[key]
    if (value != null) return value
    if (traceLocalization) println("[WARNING]  missing localization for $key, fallback is $fallback")
    return StaticText(key, fallback)
}