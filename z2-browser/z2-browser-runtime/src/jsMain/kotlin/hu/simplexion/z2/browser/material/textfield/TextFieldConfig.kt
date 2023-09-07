package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.commons.i18n.LocalizedIcon

class TextFieldConfig {
    var leadingIcon: LocalizedIcon? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var trailingIcon: LocalizedIcon? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var errorIcon: LocalizedIcon = basicIcons.error
        set(value) {
            field = value
            update?.invoke()
        }

    var onChange: ((value: String) -> Unit)? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var onEnter: (() -> Unit)? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var onEscape: (() -> Unit)? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var transparent = false
        set(value) {
            field = value
            update?.invoke()
        }

    var update : (() -> Unit)? = null
}