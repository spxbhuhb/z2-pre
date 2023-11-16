/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.material.modal

import hu.simplexion.z2.browser.css.addCss
import hu.simplexion.z2.browser.css.displayNone
import hu.simplexion.z2.browser.css.removeCss
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.vh
import hu.simplexion.z2.browser.material.vw
import hu.simplexion.z2.commons.browser.CssClass
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

/**
 * Contains modal windows and shows them over the normal content.
 */
object Modals : Z2(
    null,
    document.createElement("div") as HTMLDivElement,
    arrayOf(displayNone, CssClass("persistent")),
    { document.body?.appendChild(this.htmlElement) }
) {

    val layers = mutableListOf<Z2>()

    operator fun plusAssign(child: Z2) {
        htmlElement.removeCss(displayNone)
        layers += layer(child)
    }

    operator fun minusAssign(child: Z2) {
        val index = layers.indexOfFirst { child in it.children }
        if (index == -1) return

        while (layers.size > index) {
            layers.last().also {
                remove(it)
            }
            layers.removeLast()
        }

        if (layers.isEmpty()) htmlElement.addCss(displayNone)
    }

    fun Z2.layer(child: Z2) =
        div {
            with(style) {
                position = "fixed"
                top = 0.px
                left = 0.px
                height = 100.vh
                width = 100.vw
                justifyContent = "center"
                alignItems = "center"
                display = "flex"
                backgroundColor = "rgba(0, 0, 0, 0.5)"
                zIndex = "${1900 + layers.size}"
            }
            append(child)
        }

}