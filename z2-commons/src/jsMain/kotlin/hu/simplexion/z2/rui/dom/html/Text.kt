/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.rui.dom.html

import hu.simplexion.z2.rui.*
import org.w3c.dom.Node

@Rui
@RuiPublicApi
fun Text(content: String) {
}

@RuiPublicApi
class RuiText(
    ruiAdapter: RuiAdapter<Node>,
    ruiScope: RuiFragment<Node>?,
    ruiExternalPatch: RuiExternalPathType<Node>,
    var content: String
) : LeafNode(ruiAdapter, ruiExternalPatch) {

    override val receiver = org.w3c.dom.Text()

    var ruiDirty0 = 0L

    override val ruiScope: RuiFragment<Node>
        get() = TODO("Not yet implemented")

    @RuiPublicApi
    fun ruiInvalidate0(mask: Long) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiCreate() {
        receiver.data = content
    }

    override fun ruiPatch(dirtyMaskOfScope: Long) {
        if (ruiDirty0 and 1L != 0L) {
            receiver.data = content
        }
    }

}