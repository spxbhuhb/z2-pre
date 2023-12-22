/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui.dom

import hu.simplexion.z2.rui.RuiBridge
import hu.simplexion.z2.rui.RuiPublicApi
import org.w3c.dom.Node

/**
 * Base bridge class for W3C DOM Nodes. Web browser components such as
 * H1, Span etc. are descendants of this class.
 */
@RuiPublicApi
interface RuiDOMBridge : RuiBridge<Node> {

    override fun remove(child: RuiBridge<Node>) {
        receiver.removeChild(child.receiver)
    }

    override fun replace(oldChild: RuiBridge<Node>, newChild: RuiBridge<Node>) {
        receiver.replaceChild(oldChild.receiver, newChild.receiver)
    }

    override fun add(child: RuiBridge<Node>) {
        receiver.appendChild(child.receiver)
    }

}