package hu.simplexion.z2.localization.text

class StaticText(
    override val key : String,
    override var value : String,
) : LocalizedText {
    override fun toString() = value
}