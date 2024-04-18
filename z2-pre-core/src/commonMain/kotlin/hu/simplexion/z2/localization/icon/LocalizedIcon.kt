package hu.simplexion.z2.localization.icon

import hu.simplexion.z2.util.PublicApi

/**
 * This interface is to be used as parameter type whenever the component
 * expects an icon that should be localized.
 */
@PublicApi
interface LocalizedIcon {
    val key: String
    val value : String
}