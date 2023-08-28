package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi

/**
 * This interface is to be used as parameter type whenever the component
 * expects a textual value that should be localized. Button or menu item labels
 * for example.
 */
@PublicApi
interface LocalizedText {
    val key : String

    val support : LocalizedTextSupport?
        get() {
            if (this !is BasicLocalizedText) return null
            return this.store._support[key]
        }
}