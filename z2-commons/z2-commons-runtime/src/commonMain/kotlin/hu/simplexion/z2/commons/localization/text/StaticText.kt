package hu.simplexion.z2.commons.localization.text

class StaticText(
    override val key : String,
    override val value : String,
) : LocalizedText {
    override fun toString() = value
}