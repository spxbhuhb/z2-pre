package hu.simplexion.z2.commons.localization.text

import hu.simplexion.z2.commons.localization.LocalizationProvider
import hu.simplexion.z2.commons.localization.localizedTextStore

interface LocalizedTextProvider : LocalizationProvider {

    /**
     * Declare a static localized text.
     */
    fun static(value : String, name : String = "") =
        StaticText("direct/" + this.namespace + "/" + name, value)

    /**
     * Declare a static localized text.
     */
    fun support(supportFor : LocalizedText, value : String) = StaticText(supportFor.key + "/support", value)

    fun localized(provider : LocalizedTextProvider, name : String, default : String) : LocalizedText {
        val key = "direct/" + provider.namespace + "/" + name
        return localizedTextStore[key] ?: StaticText(key, default)
    }

}