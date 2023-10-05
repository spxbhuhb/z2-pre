package hu.simplexion.z2.commons.i18n

class BasicLocalizedIcon(
    override val key : String,
    val value : String
) : LocalizedIcon {
    override fun toString(): String = value
}