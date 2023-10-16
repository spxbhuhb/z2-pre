package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.text.LocalizedText

val Enum<*>.localized : LocalizedText
    get() {
        val key = if (this is LocalizationProvider) {
            "${this.localizationNamespace}/$name"
        } else {
            "${this::class.simpleName}/$name"
        }
        return localizationFor(key, name)
    }