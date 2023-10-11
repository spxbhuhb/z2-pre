package hu.simplexion.z2.browser.material.snackbar

import hu.simplexion.z2.localization.text.LocalizedText
import org.w3c.dom.events.Event

fun snackbar(
    message: LocalizedText,
    label: LocalizedText? = null,
    icon: Boolean = false,
    action: ((event : Event) -> Unit)? = null
) {
    Snackbars += SnackbarBase(message.toString(), label, icon, action)
}

fun snackbar(
    message: String,
    label: LocalizedText? = null,
    icon: Boolean = false,
    action: ((event : Event) -> Unit)? = null
) {
    Snackbars += SnackbarBase(message, label, icon, action)
}