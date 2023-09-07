package hu.simplexion.z2.browser.field

class FieldState {

    var label : String? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var supportText : String? = null
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
            update?.invoke()
        }

    var errorText : String? = null
        set(value) {
            field = value
            update?.invoke()
        }

    var update : (() -> Unit)? = null

}