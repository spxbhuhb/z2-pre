/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui.dom

import hu.simplexion.z2.rui.RuiAdapter
import hu.simplexion.z2.rui.RuiAdapterRegistry
import hu.simplexion.z2.rui.RuiBridge
import kotlinx.browser.window
import org.w3c.dom.Node

/**
 * The default adapter for W3C DOM nodes used in browsers.
 */
open class RuiDOMAdapter(
    val node: Node = requireNotNull(window.document.body) { "window.document.body is null or undefined" }
) : RuiAdapter<Node> {

    override val rootBridge = RuiDOMPlaceholder().also {
        node.appendChild(it.receiver)
    }

    override fun createPlaceholder(): RuiBridge<Node> {
        return RuiDOMPlaceholder()
    }

    override fun newId(): Int {
        TODO("Not yet implemented")
    }

    companion object {
        init {
            RuiAdapterRegistry.register(RuiDOMAdapterFactory)
        }
    }
}