/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveSequence<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int,
) : AdaptiveFragment<BT>(adapter, parent, index, 1) {

    override val createClosure : AdaptiveClosure<BT>
        get() = parent!!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.components + this,
        createClosure.closureSize + state.size
    )

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    val indices : IntArray
        get() = state[0] as IntArray

    override fun create() {
        if (adapter.trace) trace("create")

        patch()

        for (itemIndex in indices) {
            fragments += createClosure.owner.build(this, itemIndex)
        }
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("mount", bridge)
        fragments.forEach { it.mount(bridge) }
    }

    override fun generatedPatchInternal() {
        fragments.forEach { it.patch() }
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("unmount", bridge)
        fragments.forEach { it.unmount(bridge) }
    }

    override fun dispose() {
        if (adapter.trace) trace("dispose")
        fragments.forEach { it.dispose() }
    }

    override fun stateToTraceString(): String {
        return if (state[0] != null) indices.contentToString() else "[]"
    }
}