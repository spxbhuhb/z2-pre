/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveSequence<BT>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveClosure: AdaptiveClosure<BT>?,
    override val adaptiveParent: AdaptiveFragment<BT>,
) : AdaptiveFragment<BT> {

    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT> = {  }
    override val adaptiveState: Array<Any?> = emptyArray()

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    init {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveSequence", "init")
    }

    fun add(fragment : AdaptiveFragment<BT>) {
        fragments += fragment
    }

    override fun adaptiveCreate() {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveSequence", "create")
        for (i in fragments.indices) {
            fragments[i].adaptiveCreate()
        }
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveSequence", "mount", "bridge:", bridge)
        for (i in fragments.indices) {
            fragments[i].adaptiveMount(bridge)
        }
    }

    override fun adaptivePatch() {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveSequence", "patch")
        for (fragment in fragments) {
            fragment.adaptiveExternalPatch(fragment)
            fragment.adaptivePatch()
        }
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveSequence", "unmount", "bridge:", bridge)
        for (i in fragments.indices) {
            fragments[i].adaptiveUnmount(bridge)
        }
    }

    override fun adaptiveDispose() {
        if (adaptiveAdapter.trace) adaptiveAdapter.trace("AdaptiveSequence", "dispose")
        for (i in fragments.indices) {
            fragments[i].adaptiveDispose()
        }
    }

}