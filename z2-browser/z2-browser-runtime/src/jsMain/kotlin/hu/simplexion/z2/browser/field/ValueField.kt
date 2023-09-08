package hu.simplexion.z2.browser.field

interface ValueField<T> {
    var value : T
    val state : FieldState
}