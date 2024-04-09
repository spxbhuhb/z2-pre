/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.adaptive.dom.html

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptivePlaceholder
import org.w3c.dom.Node

fun Adaptive.Text(@Suppress("UNUSED_PARAMETER") content: String) {
}

class AdaptiveText(
    adapter: AdaptiveAdapter<Node>,
    parent : AdaptiveFragment<Node>,
    index : Int
) : LeafNode(adapter, parent, index, 1) {

    override val receiver = org.w3c.dom.Text()

    val content: String
        get() = state[0] as String

    override fun genBuild(parent: AdaptiveFragment<Node>, declarationIndex: Int): AdaptiveFragment<Node> {
        return AdaptivePlaceholder(adapter, parent, -1)
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment<Node>) {

    }

    override fun genPatchInternal() {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.data = content
        }
    }

}