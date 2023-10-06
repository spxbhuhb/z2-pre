package hu.simplexion.z2.localization.runtime

import hu.simplexion.z2.commons.localization.localizedTextStore
import hu.simplexion.z2.commons.localization.text.LocalizedText
import hu.simplexion.z2.commons.localization.text.StaticText

val Enum<*>.localized : LocalizedText
    get() {
        val key = "enum/${this::class.simpleName}/$name"
        return localizedTextStore[key] ?: StaticText(key, name)
    }