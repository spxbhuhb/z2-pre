package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.text.LocalizedText

fun Array<out Pair<LocalizedText, Any?>>.format(separator: String = ", "): String {
    val formatted = mutableListOf<String>()
    for (item in this) {
        formatted += "${item.first}=${item.second}"
    }
    return formatted.joinToString(separator)
}