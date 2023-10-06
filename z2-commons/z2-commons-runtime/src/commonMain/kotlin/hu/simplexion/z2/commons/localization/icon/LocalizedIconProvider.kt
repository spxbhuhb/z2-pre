package hu.simplexion.z2.commons.localization.icon

import hu.simplexion.z2.commons.localization.localizedIconStore

interface LocalizedIconProvider {

    /**
     * The namespace of this given icon provider. Default is "" (the empty string) which means the
     * global namespace. The keys in the global namespace start with "/". Local keys start with
     * the namespace.
     */
    val namespace : String get() = ""

    /**
     * Declare a static localized text.
     */
    fun static(value : String, key : String = "") = StaticIcon(key, value)

    fun localized(container : LocalizedIconProvider, name : String, default : String) : LocalizedIcon {
        val key = "direct/" + container.namespace + "/" + name
        return localizedIconStore[key] ?: StaticIcon(key, default)
    }

}