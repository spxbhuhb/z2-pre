package hu.simplexion.z2.browser.util

import hu.simplexion.z2.commons.i18n.BasicLocalizedText
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.LocalizedTextStore
import hu.simplexion.z2.commons.i18n.textStoreRegistry
import hu.simplexion.z2.schematic.runtime.schema.SchemaField

/**
 * Text store for fields that does not have a translation.
 */
object fieldNameFallbacks : LocalizedTextStore()

fun SchemaField<*>.label() : LocalizedText {
    for (store in textStoreRegistry) {
        val localized = store._map[name]
        if (localized != null) return localized
    }

    return fieldNameFallbacks._map.getOrPut(name) {
        BasicLocalizedText(name, name.toCamelCaseWords(), fieldNameFallbacks)
    }
}

fun String.toCamelCaseWords() : String {
    val out = mutableListOf<Char>()
    for (char in toCharArray()) {
        when {
            out.isEmpty() -> out += char.uppercaseChar()
            char.isUpperCase() -> {
                out += ' '
                out += char
            }
            else -> out += char
        }
    }
    return out.toCharArray().concatToString()
}