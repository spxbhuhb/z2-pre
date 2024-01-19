/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName", "UNUSED_PARAMETER")

package hu.simplexion.z2.rui.dom.html

import hu.simplexion.z2.rui.*
import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.Node
import org.w3c.dom.events.MouseEvent

@Rui
@RuiPublicApi
fun Button(title: String, onClick: () -> Unit) {
}

@RuiPublicApi
class RuiButton(
    ruiAdapter: RuiAdapter<Node>,
    ruiScope: RuiFragment<Node>?,
    ruiExternalPatch: RuiExternalPathType<Node>,
    var label: String,
    var onClick: (MouseEvent) -> Unit
) : LeafNode(ruiAdapter, ruiExternalPatch) {

    override val receiver = document.createElement("button") as HTMLButtonElement

    var ruiDirty0 = 0L

    override val ruiClosure: RuiClosure<Node>?
        get() = TODO("Not yet implemented")

    @RuiPublicApi
    fun ruiInvalidate0(mask: Long) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiCreate() {
        receiver.innerText = label
        receiver.onclick = onClick
    }

    override fun ruiPatch(dirtyMaskOfScope: Long) {
        if (ruiDirty0 and 1L != 0L) {
            receiver.innerText = label
        }
        if (ruiDirty0 and 2L != 0L) {
            receiver.onclick = onClick
        }
    }

}