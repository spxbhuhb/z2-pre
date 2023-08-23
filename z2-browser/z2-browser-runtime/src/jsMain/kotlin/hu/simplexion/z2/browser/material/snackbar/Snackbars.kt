/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.material.snackbar

import hu.simplexion.z2.browser.html.Z2
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

/**
 * Contains modal windows and shows them over the normal content.
 */
object Snackbars : Z2(
    null,
    document.createElement("div") as HTMLDivElement,
    emptyArray(),
    { document.body?.appendChild(this.htmlElement) }
) {

    val waiting = mutableListOf<SnackbarBase>()
    var active : SnackbarBase? = null

    operator fun plusAssign(child: SnackbarBase) {
        waiting += child
        show()
    }

    fun onHide(child: SnackbarBase) {
        remove(child)
        // active may be a different snackbar if the user
        // closes the one manually right when the timer expires
        if (active == child) {
            active = null
            show()
        }
    }

    fun show() {
        if (active != null) return
        if (waiting.isEmpty()) return

        active = waiting.removeFirst()
        active?.show()
    }
}