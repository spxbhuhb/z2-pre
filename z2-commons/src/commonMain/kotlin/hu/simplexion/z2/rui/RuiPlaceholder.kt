/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui

class RuiPlaceholder<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiParent: RuiFragment<BT>,
) : RuiFragment<BT> {

    override val ruiClosure: RuiClosure<BT>? = null
    override val ruiExternalPatch: RuiExternalPathType<BT> = {  }

    val bridge = ruiAdapter.createPlaceholder()

    override fun ruiCreate() {

    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        bridge.add(this.bridge)
    }

    override fun ruiPatch() {

    }

    override fun ruiUnmount(bridge: RuiBridge<BT>) {
        bridge.remove(this.bridge)
    }

    override fun ruiDispose() {

    }

}