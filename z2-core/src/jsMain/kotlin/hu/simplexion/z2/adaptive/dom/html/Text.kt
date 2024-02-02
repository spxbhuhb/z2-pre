/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.adaptive.dom.html

import hu.simplexion.z2.adaptive.*
import org.w3c.dom.Node

@Rui
@RuiPublicApi
fun Text(content: String) {
}

@RuiPublicApi
class RuiText(
    ruiAdapter: RuiAdapter<Node>,
    override val ruiClosure: RuiClosure<Node>?,
    override val ruiParent: RuiFragment<Node>?,
    ruiExternalPatch: RuiExternalPatchType<Node>,
    var content: String,
) : LeafNode(ruiAdapter, ruiExternalPatch) {

    override val receiver = org.w3c.dom.Text()

    var ruiDirty0 = 0L

    @RuiPublicApi
    fun ruiInvalidate0(mask: Long) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiCreate() {
        receiver.data = content
    }

    override fun ruiPatch() {
        if (ruiDirty0 and 1L != 0L) {
            receiver.data = content
        }
    }

}