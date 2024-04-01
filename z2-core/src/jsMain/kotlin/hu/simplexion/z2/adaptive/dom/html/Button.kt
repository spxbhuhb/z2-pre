/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName", "UNUSED_PARAMETER")

package hu.simplexion.z2.adaptive.dom.html

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptiveSupportFunction
import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.Node

fun Adaptive.Button(title: String, onClick: () -> Unit) {
}

class AdaptiveButton(
    adapter: AdaptiveAdapter<Node>,
    parent : AdaptiveFragment<Node>,
    index : Int
) : LeafNode(adapter, parent, index, 2) {

    val label: String
        get() = state[0] as String

    val onClick : AdaptiveSupportFunction<*>
        get() = state[1] as AdaptiveSupportFunction<*>

    override val receiver = document.createElement("button") as HTMLButtonElement

    override fun patchInternal() {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.innerText = label
        }

        if (haveToPatch(closureMask, 2)) {
            receiver.onclick = { onClick.invoke() }
        }
    }

}