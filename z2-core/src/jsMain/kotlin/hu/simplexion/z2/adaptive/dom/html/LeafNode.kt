/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.dom.html

import hu.simplexion.z2.adaptive.*
import org.w3c.dom.Node

/**
 * Leaf nodes in HTML are nodes that does not have any children. A text or an image
 * is a good example.
 */
abstract class LeafNode(
    adapter: AdaptiveAdapter<Node>,
    parent: AdaptiveFragment<Node>?,
    index: Int,
    stateSize : Int
) : AdaptiveGeneratedFragment<Node>(adapter, parent, index, stateSize), AdaptiveBridge<Node> {

    override fun build(parent: AdaptiveFragment<Node>, declarationIndex: Int): AdaptiveFragment<Node>? {
        shouldNotRun()
    }

    override fun patchDescendant(fragment: AdaptiveFragment<Node>) {
        shouldNotRun()
    }

    override fun invoke(supportFunction: AdaptiveSupportFunction<Node>, arguments: Array<out Any?>): Any? {
        shouldNotRun()
    }

    override fun remove(child: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }

    override fun replace(oldChild: AdaptiveBridge<Node>, newChild: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }

    override fun add(child: AdaptiveBridge<Node>) {
        throw IllegalStateException()
    }

    override fun mount(bridge: AdaptiveBridge<Node>) {
        bridge.add(this)
    }

    override fun unmount(bridge: AdaptiveBridge<Node>) {
        bridge.remove(this)
    }

    override fun dispose() {

    }
}