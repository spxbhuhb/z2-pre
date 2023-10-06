package hu.simplexion.z2.commons.localization

interface LocalizationProvider {

    /**
     * The namespace of this given text provider. Default is "" (the empty string) which means the
     * global namespace. The keys in the global namespace start with "/". Local keys start with
     * the namespace.
     */
    val namespace : String get() = ""

}