/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.material.modal

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.vh
import hu.simplexion.z2.browser.material.vw
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLDivElement

/**
 * Contains modal windows and shows them over the normal content.
 */
object Modals : Z2(
    null,
    document.createElement("div") as HTMLDivElement,
    arrayOf("hidden", "persistent"),
    { document.body?.appendChild(this.htmlElement) }
) {

    val layers = mutableListOf<Z2>()

    operator fun plusAssign(child: Z2) {
        htmlElement.removeClass("hidden")
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

        if (layers.isEmpty()) htmlElement.addClass("hidden")
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