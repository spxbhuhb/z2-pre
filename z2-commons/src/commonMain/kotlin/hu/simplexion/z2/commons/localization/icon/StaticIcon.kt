package hu.simplexion.z2.commons.localization.icon

class StaticIcon(
    override val key : String,
    override val value : String
) : LocalizedIcon {
    override fun toString() = value
}