package hu.simplexion.z2.i18n.runtime

import hu.simplexion.z2.commons.i18n.textStoreRegistry

val Enum<*>.localized : String
    get() {
        val key = this::class.simpleName + "." + name
        for (store in textStoreRegistry) {
            val localized = store._map[key]
            if (localized != null) return localized.toString()
        }
        return name
    }