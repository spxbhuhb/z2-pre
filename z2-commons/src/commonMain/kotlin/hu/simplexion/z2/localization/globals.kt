package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText

var traceLocalization : Boolean = false

val localizedTextStore = mutableMapOf<String, LocalizedText>()

val localizedIconStore = mutableMapOf<String, LocalizedIcon>()