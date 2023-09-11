package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.commons.i18n.LocalizedIcon

class TextFieldConfig(
    val style : FieldStyle = defaultFieldStyle
) {

    companion object {
        var defaultFieldStyle = FieldStyle.Transparent
    }

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

    var errorIcon: LocalizedIcon = browserIcons.error
        set(value) {
            field = value
            update?.invoke()
        }

    var onChange: (FilledTextField.(value: String) -> Unit)? = null
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

    var update : (() -> Unit)? = null

}