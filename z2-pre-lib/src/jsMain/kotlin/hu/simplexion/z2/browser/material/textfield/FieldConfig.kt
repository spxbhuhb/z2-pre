package hu.simplexion.z2.browser.material.textfield

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.localization.icon.LocalizedIcon

open class FieldConfig<T>(
    val style : FieldStyle = defaultFieldStyle,
    var decodeFromString : (value : String) -> T?,
    onChange : ((field : AbstractField<T>) -> Unit)? = null
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

    var onChange = onChange
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

    var encodeToString : (value : T) -> String = { it.toString() }

    var update : (() -> Unit)? = null

}