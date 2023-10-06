package hu.simplexion.z2.commons.localization.text

import hu.simplexion.z2.commons.localization.localizedTextStore

interface LocalizedTextProvider {

    /**
     * The namespace of this given text provider. Default is "" (the empty string) which means the
     * global namespace. The keys in the global namespace start with "/". Local keys start with
     * the namespace.
     */
    val namespace : String get() = ""

    /**
     * Declare a static localized text.
     */
    fun static(value : String, key : String = "") = StaticText(key, value)

    /**
     * Declare a static localized text.
     */
    fun support(supportFor : LocalizedText, value : String) = StaticText(supportFor.key + "/support", value)

    fun localized(container : LocalizedTextProvider, name : String, default : String) : LocalizedText {
        val key = "direct/" + container.namespace + "/" + name
        return localizedTextStore[key] ?: StaticText(key, default)
    }

}