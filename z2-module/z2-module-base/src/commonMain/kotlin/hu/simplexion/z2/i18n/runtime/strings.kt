package hu.simplexion.z2.i18n.runtime

import hu.simplexion.z2.commons.i18n.BasicLocalizedText
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.textStoreRegistry

val String.localized: LocalizedText
    get() {
        for (store in textStoreRegistry) {
            val localized = store._map[this]
            if (localized != null) return localized
        }
        return BasicLocalizedText(this, this, null)
    }
