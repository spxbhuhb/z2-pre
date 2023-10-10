package hu.simplexion.z2.commons.localization.text

import hu.simplexion.z2.commons.localization.LocalizationProvider
import hu.simplexion.z2.commons.localization.localizedTextStore

interface LocalizedTextProvider : LocalizationProvider {

    /**
     * Declare a static localized text.
     */
    fun static(value : String, support : String? = null,  name : String = "") : StaticText {
        val key = "direct/" + this.localizationNamespace + "/" + name

        val existing =  localizedTextStore[key]

        if (existing != null) {
            require(existing is StaticText) { "localized text type mismatch for $key ${existing::class.simpleName} vs StaticText" }
            return existing
        }

        support?.let {
            val supportKey = "$key/support"
            localizedTextStore[supportKey] = StaticText(supportKey, support)
        }

        return StaticText(key, value).also { localizedTextStore[key] = it }
    }

    fun localized(provider : LocalizedTextProvider, name : String, default : String) : LocalizedText {
        val key = "direct/" + provider.localizationNamespace + "/" + name
        return localizedTextStore[key] ?: StaticText(key, default)
    }

}