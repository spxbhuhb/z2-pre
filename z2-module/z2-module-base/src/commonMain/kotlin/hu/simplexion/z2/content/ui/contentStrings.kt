package hu.simplexion.z2.content.ui

import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider

interface ContentStrings : LocalizedTextProvider {
    val upload get() = static("feltöltés")
    val download get() = static("letöltés")
}
