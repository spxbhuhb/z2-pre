/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveWhen<BT>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveClosure: AdaptiveClosure<BT>?,
    override val adaptiveParent: AdaptiveFragment<BT>,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT>,
    val factory: AdaptiveFragmentFactory<BT>
) : AdaptiveFragment<BT> {

    val placeholder: AdaptiveBridge<BT> = adaptiveAdapter.createPlaceholder()

    var newBranch = -1 // this is changed by external patch
    var branch = -1 // -1 means that there is nothing to add, like if (condition) div {  }

    var fragment: AdaptiveFragment<BT>? = null

    init {
        adaptiveExternalPatch(this)
        if (newBranch != branch) {
            fragment = factory(this, branch)
        }
    }

    override fun adaptiveCreate() {
        fragment?.adaptiveCreate()
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        bridge.add(placeholder)
        fragment?.adaptiveMount(placeholder)
    }

    override fun adaptivePatch() {
        if (newBranch == branch) {
            fragment?.let {
                it.adaptiveExternalPatch(it)
                it.adaptivePatch()
            }
        } else {
            fragment?.adaptiveUnmount(placeholder)
            fragment?.adaptiveDispose()
            branch = newBranch
            fragment = factory(this, branch)
            fragment?.adaptiveCreate()
            fragment?.adaptiveMount(placeholder)
        }
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        fragment?.adaptiveUnmount(placeholder)
        bridge.remove(placeholder)
    }

    override fun adaptiveDispose() {
        fragment?.adaptiveDispose()
    }

}