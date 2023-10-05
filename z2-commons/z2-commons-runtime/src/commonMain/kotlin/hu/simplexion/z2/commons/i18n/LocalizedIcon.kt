package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi

/**
 * This interface is to be used as parameter type whenever the component
 * expects an icon that should be localized.
 */
@PublicApi
interface LocalizedIcon {
    val key: String
}