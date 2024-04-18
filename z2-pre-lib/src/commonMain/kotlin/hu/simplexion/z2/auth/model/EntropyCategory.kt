package hu.simplexion.z2.auth.model

enum class EntropyCategory(
    val min: Int,
    val max: Int
) {
    Poor(0, 24),
    Weak(25, 49),
    Reasonable(50, 74),
    Excellent(75, 100)
}