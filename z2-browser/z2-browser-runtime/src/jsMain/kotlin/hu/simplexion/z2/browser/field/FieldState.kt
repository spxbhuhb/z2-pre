package hu.simplexion.z2.browser.field

import hu.simplexion.z2.commons.i18n.LocalizedText

class FieldState(
    label : String? = null,
    supportText : String? = null
) {

    constructor(label : LocalizedText) : this(label.toString(), label.support?.toString())

    var label = label
        set(value) {
            field = value
            update?.invoke()
        }

    var supportText = supportText
        set(value) {
            field = value
            update?.invoke()
        }

    var touched = false
        set(value) {
            field = value
            update?.invoke()
        }

    var disabled = false
        set(value) {
            field = value
            update?.invoke()
        }

    var readOnly = false
        set(value) {
            field = value
            update?.invoke()
        }

    var error = false
        set(value) {
            field = value
            touched = true
        }

    var errorText : String? = null
        set(value) {
            field = value
            touched = true
        }

    var update : (() -> Unit)? = null

}