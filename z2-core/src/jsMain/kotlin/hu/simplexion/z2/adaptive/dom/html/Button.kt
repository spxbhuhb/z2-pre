/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName", "UNUSED_PARAMETER")

package hu.simplexion.z2.adaptive.dom.html

import hu.simplexion.z2.adaptive.*
import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.Node
import org.w3c.dom.events.MouseEvent

@Adaptive
@AdaptivePublicApi
fun Button(title: String, onClick: () -> Unit) {
}

@AdaptivePublicApi
class AdaptiveButton(
    adaptiveAdapter: AdaptiveAdapter<Node>,
    override val adaptiveParent: AdaptiveFragment<Node>?,
    adaptiveScope: AdaptiveFragment<Node>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<Node>,
    var label: String,
    var onClick: (MouseEvent) -> Unit,
) : LeafNode(adaptiveAdapter, adaptiveExternalPatch) {

    override val receiver = document.createElement("button") as HTMLButtonElement

    var adaptiveDirty0 = 0L

    override val adaptiveClosure: AdaptiveClosure<Node>?
        get() = TODO("Not yet implemented")

    @AdaptivePublicApi
    fun adaptiveInvalidate0(mask: Long) {
        adaptiveDirty0 = adaptiveDirty0 or mask
    }

    override fun adaptiveCreate() {
        receiver.innerText = label
        receiver.onclick = onClick
    }

    override fun adaptivePatch() {
        if (adaptiveDirty0 and 1L != 0L) {
            receiver.innerText = label
        }
        if (adaptiveDirty0 and 2L != 0L) {
            receiver.onclick = onClick
        }
    }

}