package hu.simplexion.z2.browser.material.snackbar

import hu.simplexion.z2.localization.text.LocalizedText
import org.w3c.dom.events.Event

fun snackbar(
    message: LocalizedText,
    label: LocalizedText? = null,
    icon: Boolean = false,
    delay: Long = 2000,
    action: ((event : Event) -> Unit)? = null
) {
    Snackbars += SnackbarBase(message.toString(), label, icon, delay, action)
}

fun snackbar(
    message: String,
    label: LocalizedText? = null,
    icon: Boolean = false,
    delay: Long = 2000,
    action: ((event : Event) -> Unit)? = null
) {
    Snackbars += SnackbarBase(message, label, icon, delay, action)
}

fun warningSnackbar(
    message: LocalizedText,
    label: LocalizedText? = null,
    icon: Boolean = true,
    delay: Long = 5000,
    action: ((event : Event) -> Unit)? = null
) {
    Snackbars += SnackbarBase(message.toString(), label, icon, delay, action).also {
        it.style.backgroundColor = "#ffef00"
        it.style.color = "black"
    }
}

fun errorSnackbar(
    message: LocalizedText,
    label: LocalizedText? = null,
    icon: Boolean = true,
    delay: Long = 15000,
    action: ((event : Event) -> Unit)? = null
) {
    Snackbars += SnackbarBase(message.toString(), label, icon, delay, action).also {
        it.style.backgroundColor = "var(--md-sys-color-error)"
        it.style.color = "var(--md-sys-color-on-error)"
    }
}