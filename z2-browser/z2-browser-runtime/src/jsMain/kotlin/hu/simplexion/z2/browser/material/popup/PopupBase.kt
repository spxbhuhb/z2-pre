/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.material.popup

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.onFocusOut
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.FocusEvent

open class PopupBase(
    val anchor: Z2,
    val minHeight: Double = 100.0,
    val minWidth: Double = 100.0,
    builder: Z2.() -> Unit
) : Z2(
    null,
    document.createElement("div") as HTMLElement,
    arrayOf("popup"),
    builder
) {

    var shown: Boolean = false

    init {
        onFocusOut { event ->
            event as FocusEvent
            val relatedTarget = event.relatedTarget

            if (relatedTarget is HTMLElement && htmlElement.contains(relatedTarget)) {
                //onClick(relatedTarget)
            } else {
                hide()
            }
        }
    }

    open fun toggle() {
        if (shown) {
            hide()
        } else {
            show()
        }
    }

    open fun show() {
        shown = true
        Popups.append(this)
        alignPopup(this, anchor, minHeight, minWidth)
        htmlElement.tabIndex = 0
        htmlElement.focus()
    }

    fun hide() {
        if (shown) {
            shown = false
            Popups.remove(this)
        }
    }

}