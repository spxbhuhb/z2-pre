package hu.simplexion.z2.localization.text

import hu.simplexion.z2.localization.LocalizationProvider
import hu.simplexion.z2.localization.localizationFor
import hu.simplexion.z2.localization.localizedTextStore

interface LocalizedTextProvider : LocalizationProvider {

    /**
     * Declare a static localized text.
     */
    fun static(value : String, support : String? = null,  name : String = "") : StaticText {
        val key = this.localizationNamespace + "/" + name

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

    fun localized(provider : LocalizedTextProvider, name : String, default : String) : LocalizedText =
        localizationFor(provider.localizationNamespace + "/" + name, default)

}