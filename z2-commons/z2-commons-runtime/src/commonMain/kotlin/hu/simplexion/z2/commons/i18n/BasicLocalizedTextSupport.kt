package hu.simplexion.z2.commons.i18n

class BasicLocalizedTextSupport(
    override val key : String,
    override val supportFor: String,
    val value : String
) : LocalizedTextSupport {
    override fun toString(): String = value
}