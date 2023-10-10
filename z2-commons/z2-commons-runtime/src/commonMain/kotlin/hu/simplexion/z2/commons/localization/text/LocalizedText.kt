package hu.simplexion.z2.commons.localization.text

import hu.simplexion.z2.commons.localization.localizedTextStore
import hu.simplexion.z2.commons.util.PublicApi

/**
 * This interface is to be used as parameter type whenever the component
 * expects a textual value that should be localized. Button or menu item labels
 * for example.
 */
@PublicApi
interface LocalizedText {
    val key: String
    val value: String

    val support
        get() = localizedTextStore["$key/support"]

    val help
        get() = localizedTextStore["$key/help"]
}