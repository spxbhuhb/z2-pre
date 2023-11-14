/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.material.modal

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.button.filledLaunchButton
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.util.EditMode
import hu.simplexion.z2.commons.util.localLaunch
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.text.LocalizedText
import kotlinx.browser.document
import kotlinx.coroutines.channels.Channel
import org.w3c.dom.HTMLElement

/**
 * @property  channel    Receives output of the modal dialog (if there is one).
 *                       Pass a nullable type if the dialog may be closed without
 *                       output.
 */
open class ModalBase<T : Any?>(
    val mainBuilder : ModalBase<T>.() -> Unit
) : Z2(
    null,
    document.createElement("div") as HTMLElement,
    arrayOf(boxSizingBorderBox, onSurfaceText, surface, borderRadius12)
) {
    var defaultModalGrid = true
    var editMode = EditMode.Unspecified
    val channel = Channel<T>(1)

    override fun main() : ModalBase<T> {
        if (defaultModalGrid) {
            addClass(displayGrid)
            gridTemplateColumns = "1fr"
            gridTemplateRows = "min-content 1fr min-content"
        }
        mainBuilder() // FIXME clear up the confusion between builder and main
        return this
    }

    fun Z2.title(title : String) =
        div(pb16, pt16, mb24, displayFlex, alignItemsCenter, headlineSmall, borderBottomOutlineVariant) {
            span(pl24, pr24) { + title.localeCapitalized }
        }

    fun Z2.title(title : LocalizedText) =
        div(pb16, pt16, mb24, displayFlex, alignItemsCenter, headlineSmall, borderBottomOutlineVariant) {
            span(pl24, pr24) { + title.localeCapitalized }
        }

    fun Z2.title(addTitle : LocalizedText, editTitle : LocalizedText) {
        when (editMode) {
            EditMode.Unspecified -> throw IllegalStateException("no mode specified for the modal")
            EditMode.Add -> title(addTitle)
            EditMode.Edit -> title(editTitle)
        }
    }

    fun Z2.supportingText(builder : () -> String) : Z2 =
        div(p24, pt0, bodyMedium) {
            text { builder() }
        }

    fun Z2.body(builder : Z2Builder) =
        div(pl24, pr24, positionRelative) {
            builder()
        }

    fun Z2.buttons(builder : Z2Builder) : Z2 =
        div(mt24, borderTopOutlineVariant) {
            grid(pl16, pr16, pt12, pb12, gridAutoFlowColumn, gridAutoColumnsMinContent, gridGap16, justifyContentSpaceBetween) {
                builder()
            }
        }

    @Deprecated("use open instead", replaceWith = ReplaceWith("open"))
    fun launchShow() {
        localLaunch { show() }
    }

    open suspend fun show(): T {
        Modals += this
        return waitFor()
    }

    fun closeWith(value : T) {
        channel.trySend(value)
    }

    suspend fun waitFor() : T {
        val value = channel.receive()
        Modals -= this
        dispose() // TODO think about modal dispose and reuse (probably shouldn't allow reuse)
        return value
    }

    // --------------------------------------------------------------------------
    // Mode convenience
    // --------------------------------------------------------------------------

    inline fun editModeIf(condition : () -> Boolean) {
        editMode = if (condition()) EditMode.Edit else EditMode.Add
    }

    // --------------------------------------------------------------------------
    // Unit value modal convenience
    // --------------------------------------------------------------------------

    fun ModalBase<Unit>.open() {
        localLaunch { show() }
    }

    fun ModalBase<Unit>.save(label : LocalizedText? = null, saveFun : suspend () -> Unit) {
        buttons {
            textButton(browserStrings.cancel) { closeWith(Unit) }
            filledLaunchButton(label ?: saveLabel()) {
                saveFun()
            }
        }
    }

    fun ModalBase<Unit>.close() {
        closeWith(Unit)
    }

    fun saveLabel() : LocalizedText =
        when (editMode) {
            EditMode.Unspecified -> browserStrings.ok
            EditMode.Add -> browserStrings.add
            EditMode.Edit -> browserStrings.save
        }
}