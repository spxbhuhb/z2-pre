package hu.simplexion.z2.localization.icon

import hu.simplexion.z2.localization.LocalizationProvider
import hu.simplexion.z2.localization.localizedIconStore

interface LocalizedIconProvider  : LocalizationProvider {

    /**
     * Declare a static localized text.
     */
    fun static(value : String, hint : String? = null, key : String = "") = StaticIcon(key, value)

    fun localized(container : LocalizedIconProvider, name : String, default : String) : LocalizedIcon {
        val key = "direct/" + container.localizationNamespace + "/" + name
        return localizedIconStore[key] ?: StaticIcon(key, default)
    }

}