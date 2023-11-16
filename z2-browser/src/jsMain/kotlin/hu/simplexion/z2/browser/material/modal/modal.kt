package hu.simplexion.z2.browser.material.modal

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.CssClass
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.util.EditMode
import hu.simplexion.z2.commons.util.localLaunch
import hu.simplexion.z2.localization.text.LocalizedText

/**
 * Create and open a modal without a return value, built by [builder].
 */
fun modal(vararg classes: CssClass, builder: ModalBase<Unit>.() -> Unit): ModalBase<Unit> =
    modal(classes, EditMode.Unspecified, builder)

/**
 * Create and open a modal for adding items, built by [builder].
 */
fun addModal(vararg classes: CssClass, builder: ModalBase<Unit>.() -> Unit): ModalBase<Unit> =
    modal(classes, EditMode.Add, builder)

/**
 * Create and open the modal for editing items, built by [builder].
 */
fun editModal(vararg classes: CssClass, builder: ModalBase<Unit>.() -> Unit): ModalBase<Unit> =
    modal(classes, EditMode.Edit, builder)

/**
 * Create and open the modal with a specific edit mode, built by [builder].
 */
fun modal(classes: Array<out CssClass>, editMode: EditMode, builder: ModalBase<Unit>.() -> Unit): ModalBase<Unit> =
    ModalBase(builder).apply {
        addCss(*classes)
        this.editMode = editMode
        main()
        open()
    }

/**
 * Create and open the modal with a specific edit mode, built by [builder].
 */
fun <T : Any?> inputModal(vararg classes : CssClass, builder: ModalBase<T>.() -> Unit): ModalBase<T> =
    ModalBase(builder).apply {
        addCss(*classes)
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