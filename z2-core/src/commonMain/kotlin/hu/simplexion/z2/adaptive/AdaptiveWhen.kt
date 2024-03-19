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
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveWhen", "init", "newBranch:", newBranch)
        if (newBranch != branch) {
            branch = newBranch
            fragment = factory(this, branch)
        }
    }

    override fun adaptiveCreate() {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveWhen", "create")
        fragment?.adaptiveCreate()
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveWhen", "mount", "bridge:", bridge)
        bridge.add(placeholder)
        fragment?.adaptiveMount(placeholder)
    }

    override fun adaptivePatch() {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveWhen", "patch", "branch:", branch, "newBranch:", newBranch)
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
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveWhen", "unmount", "bridge:", bridge)
        fragment?.adaptiveUnmount(placeholder)
        bridge.remove(placeholder)
    }

    override fun adaptiveDispose() {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveWhen", "dispose")
        fragment?.adaptiveDispose()
    }

}