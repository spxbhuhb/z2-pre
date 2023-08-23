package hu.simplexion.z2.browser.material.modal

import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.util.localLaunch

fun <T : Any?> modal(vararg classes: String, builder: ModalBase<T>.() -> Unit): ModalBase<T> =
    ModalBase(builder).apply { addClass(*classes) }

fun confirm(
    title: LocalizedText,
    message: LocalizedText,
    noLabel: LocalizedText = basicStrings.cancel,
    yesLabel: LocalizedText = basicStrings.yes,
    action: suspend () -> Unit
) {
    localLaunch {
        if (confirm(title, message, noLabel, yesLabel)) action()
    }
}

suspend fun confirm(
    title: LocalizedText,
    message: LocalizedText,
    noLabel: LocalizedText = basicStrings.cancel,
    yesLabel: LocalizedText = basicStrings.yes
) : Boolean =

    modal {
        title(title)
        supportingText { message.toString() }
        buttons {
            textButton(noLabel) { channel.trySend(false) }
            textButton(yesLabel) { channel.trySend(true) }
        }
    }.show()