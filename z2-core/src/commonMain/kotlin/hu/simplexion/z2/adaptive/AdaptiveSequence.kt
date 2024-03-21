/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveSequence<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val closure: AdaptiveClosure<BT>?,
    override val parent: AdaptiveFragment<BT>,
    override val index: Int,
    vararg val childIndices: Array<Int>
) : AdaptiveStructuralFragment<BT> {

    override val state: Array<Any?> = emptyArray()

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    override fun adaptiveCreate() {
        if (adapter.trace) adapter.trace("AdaptiveSequence", "create")

        fragments.forEach { it.adaptiveCreate() }
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace("AdaptiveSequence", "mount", "bridge:", bridge)
        fragments.forEach { it.adaptiveMount(bridge) }
    }

    override fun adaptiveExternalPatch() {
        if (adapter.trace) adapter.trace("AdaptiveSequence", "adaptiveExternalPatch")
        fragments.forEach { it.adaptiveExternalPatch() }
    }

    override fun adaptiveInternalPatch() {
        if (adapter.trace) adapter.trace("AdaptiveSequence", "adaptiveInternalPatch")
        fragments.forEach { it.adaptiveInternalPatch() }
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace("AdaptiveSequence", "unmount", "bridge:", bridge)
        fragments.forEach { it.adaptiveUnmount(bridge) }
    }

    override fun adaptiveDispose() {
        if (adapter.trace) adapter.trace("AdaptiveSequence", "dispose")
        fragments.forEach { it.adaptiveDispose() }
    }


}