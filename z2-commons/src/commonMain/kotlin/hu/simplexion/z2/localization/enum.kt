package hu.simplexion.z2.localization

val Enum<*>.localized : String
    get() {
        if (this is LocalizationProvider) {
            localizedTextStore["${this.localizationNamespace}/$name"]?.let { return it.value }
        }
        localizedTextStore["$fallbackNamespace/$name"]?.let { return it.value }
        return name
    }