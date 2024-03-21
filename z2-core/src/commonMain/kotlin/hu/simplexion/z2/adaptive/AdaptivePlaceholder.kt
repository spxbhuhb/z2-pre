/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptivePlaceholder<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>,
) : AdaptiveFragment<BT> {

    override val closure: AdaptiveClosure<BT>? = null
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT> = {  }
    override val state: Array<Any?> = emptyArray()

    val bridge = adapter.createPlaceholder()

    override fun adaptiveCreate() {

    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        bridge.add(this.bridge)
    }

    override fun adaptiveInternalPatch() {

    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        bridge.remove(this.bridge)
    }

    override fun adaptiveDispose() {

    }

}