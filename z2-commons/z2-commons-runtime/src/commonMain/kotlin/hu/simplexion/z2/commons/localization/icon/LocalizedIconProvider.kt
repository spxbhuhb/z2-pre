package hu.simplexion.z2.commons.localization.icon

import hu.simplexion.z2.commons.localization.LocalizationProvider
import hu.simplexion.z2.commons.localization.localizedIconStore

interface LocalizedIconProvider  : LocalizationProvider {

    /**
     * Declare a static localized text.
     */
    fun static(value : String, key : String = "") = StaticIcon(key, value)

    fun localized(container : LocalizedIconProvider, name : String, default : String) : LocalizedIcon {
        val key = "direct/" + container.namespace + "/" + name
        return localizedIconStore[key] ?: StaticIcon(key, default)
    }

}