/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptivePlaceholder<BT>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveParent: AdaptiveFragment<BT>,
) : AdaptiveFragment<BT> {

    override val adaptiveClosure: AdaptiveClosure<BT>? = null
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT> = {  }
    override val adaptiveState: Array<Any?> = emptyArray()

    val bridge = adaptiveAdapter.createPlaceholder()

    override fun adaptiveCreate() {

    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        bridge.add(this.bridge)
    }

    override fun adaptivePatch() {

    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        bridge.remove(this.bridge)
    }

    override fun adaptiveDispose() {

    }

}