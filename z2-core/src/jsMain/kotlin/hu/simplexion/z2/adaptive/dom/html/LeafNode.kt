/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.dom.html

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveBridge
import hu.simplexion.z2.adaptive.AdaptiveExternalPatchType
import hu.simplexion.z2.adaptive.AdaptiveFragment
import org.w3c.dom.Node

/**
 * Leaf nodes in HTML are nodes that does not have any children. A text or an image
 * is a good example.
 */
abstract class LeafNode(
    override val adaptiveAdapter: AdaptiveAdapter<Node>,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<Node>,
) : AdaptiveFragment<Node>, AdaptiveBridge<Node> {


    override fun remove(child: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }

    override fun replace(oldChild: AdaptiveBridge<Node>, newChild: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }

    override fun add(child: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<Node>) {
        bridge.add(this)
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<Node>) {
        bridge.remove(this)
    }

    override fun adaptiveDispose() {

    }
}