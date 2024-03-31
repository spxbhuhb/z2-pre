/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveSequence<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val index: Int,
) : AdaptiveStructuralFragment<BT> {

    override val id = adapter.newId()

    // 0 : Array<Int>, the indices of sequence items
    override val state = arrayOfNulls<Any?>(1)

    override var dirtyMask = adaptiveInitStateMask

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    override fun create() {
        if (adapter.trace) trace("create")

        patchExternal()
        // no internals for structural fragments

        for (itemIndex in state[0] as IntArray) {
            fragments += createClosure.owner.build(this, itemIndex)
        }

        dirtyMask = adaptiveCleanStateMask
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("mount", bridge)
        fragments.forEach { it.mount(bridge) }
    }

    override fun patchInternal() {
        if (adapter.trace) traceWithState("beforePatchInternal")
        fragments.forEach { it.patch() }
        dirtyMask = adaptiveCleanStateMask
        if (adapter.trace) traceWithState("afterPatchInternal")
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("unmount", bridge)
        fragments.forEach { it.unmount(bridge) }
    }

    override fun dispose() {
        if (adapter.trace) trace("dispose")
        fragments.forEach { it.dispose() }
    }

    override fun traceWithState(point : String) {
        adapter.trace(this, point, "closureDirtyMask: ${getClosureDirtyMask()} state: ${(state[0] as? IntArray).contentToString()}")
    }
}