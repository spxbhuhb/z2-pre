package hu.simplexion.z2.browser.immaterial.button

import hu.simplexion.z2.localization.text.LocalizedText

data class SelectButtonState<T>(
    val options : List<T>,
    val value : T,
    val label : LocalizedText? = null,
    val onSelectFun : (T) -> Unit
)