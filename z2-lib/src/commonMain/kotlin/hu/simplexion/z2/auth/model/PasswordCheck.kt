package hu.simplexion.z2.auth.model

enum class PasswordCheck {
    Length,
    Uppercase,
    Digit,
    Special,
    Same,
    Strength
}