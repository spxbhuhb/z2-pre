package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.commons.i18n.LocalizedIcon

class TextFieldConfig {
    var leadingIcon: LocalizedIcon? = null
    var trailingIcon: LocalizedIcon? = null
    var errorIcon: LocalizedIcon = basicIcons.error
    var onChange: ((value: String) -> Unit)? = null
    var onEnter: (() -> Unit)? = null
    var onEscape: (() -> Unit)? = null
}