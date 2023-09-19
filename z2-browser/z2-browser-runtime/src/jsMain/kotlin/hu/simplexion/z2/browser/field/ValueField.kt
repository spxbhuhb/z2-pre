package hu.simplexion.z2.browser.field

interface ValueField<T> {
    var value : T
    var valueOrNull : T?
    val state : FieldState
}