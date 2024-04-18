package hu.simplexion.z2.localization.runtime

import hu.simplexion.z2.localization.localizedTextStore
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.localization.text.StaticText

val String.localized: LocalizedText
    get() {
        return localizedTextStore[this] ?: StaticText("", this)
    }
