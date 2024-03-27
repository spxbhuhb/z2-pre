/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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

    override var dirtyMask = 0

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    override fun create() {
        if (adapter.trace) adapter.trace("AdaptiveSequence", id, "create")

        createClosure.owner.patch(this)

        @Suppress("UNCHECKED_CAST")
        for (itemIndex in state[0] as Array<Int>) {
            fragments += createClosure.owner.build(this, itemIndex)
        }
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace("AdaptiveSequence", id, "mount", "bridge:", bridge)
        fragments.forEach { it.mount(bridge) }
    }

    override fun patch() {
        if (adapter.trace) adapter.trace("AdaptiveSequence", id, "adaptiveInternalPatch")
        fragments.forEach { it.patch() }
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace("AdaptiveSequence", id, "unmount", "bridge:", bridge)
        fragments.forEach { it.unmount(bridge) }
    }

    override fun dispose() {
        if (adapter.trace) adapter.trace("AdaptiveSequence", id, "dispose")
        fragments.forEach { it.dispose() }
    }


}