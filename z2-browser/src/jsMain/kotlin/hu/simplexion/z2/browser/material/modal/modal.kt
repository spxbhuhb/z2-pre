package hu.simplexion.z2.browser.material.modal

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.util.EditMode
import hu.simplexion.z2.commons.util.localLaunch
import hu.simplexion.z2.localization.text.LocalizedText

/**
 * Create and open a modal without a return value, built by [builder].
 */
fun modal(vararg classes: String, builder: ModalBase<Unit>.() -> Unit): ModalBase<Unit> =
    modal(classes, EditMode.Unspecified, builder)

/**
 * Create and open a modal for adding items, built by [builder].
 */
fun addModal(vararg classes: String, builder: ModalBase<Unit>.() -> Unit): ModalBase<Unit> =
    modal(classes, EditMode.Add, builder)

/**
 * Create and open the modal for editing items, built by [builder].
 */
fun editModal(vararg classes: String, builder: ModalBase<Unit>.() -> Unit): ModalBase<Unit> =
    modal(classes, EditMode.Edit, builder)

/**
 * Create and open the modal with a specific edit mode, built by [builder].
 */
fun modal(classes: Array<out String>, editMode: EditMode, builder: ModalBase<Unit>.() -> Unit): ModalBase<Unit> =
    ModalBase(builder).apply {
        addClass(*classes)
        this.editMode = editMode
        main()
        open()
    }

/**
 * Create and open the modal with a specific edit mode, built by [builder].
 */
fun <T : Any?> inputModal(vararg classes : String, builder: ModalBase<T>.() -> Unit): ModalBase<T> =
    ModalBase(builder).apply {
        addClass(*classes)
        main()
    }

fun confirm(
    title: LocalizedText,
    message: LocalizedText,
    noLabel: LocalizedText = browserStrings.cancel,
    yesLabel: LocalizedText = browserStrings.yes,
    action: suspend () -> Unit
) {
    localLaunch {
        if (confirm(title, message, noLabel, yesLabel)) action()
    }
}

suspend fun confirm(
    title: LocalizedText,
    message: LocalizedText,
    noLabel: LocalizedText = browserStrings.cancel,
    yesLabel: LocalizedText = browserStrings.yes
): Boolean =
    inputModal {
        title(title)
        supportingText { message.toString() }
        buttons {
            textButton(noLabel) { channel.trySend(false) }
            textButton(yesLabel) { channel.trySend(true) }
        }
    }.show()