package hu.simplexion.z2.commons.i18n

class BasicLocalizedText(
    override val key : String,
    val value : String,
    val store: LocalizedTextStore
) : LocalizedText {
    override fun toString(): String = value
}