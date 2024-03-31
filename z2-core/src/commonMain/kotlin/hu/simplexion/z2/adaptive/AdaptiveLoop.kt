/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveLoop<BT, IT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val index: Int
) : AdaptiveStructuralFragment<BT> {

    override val id = adapter.newId()

    // 0: AdaptiveIteratorFactory
    // 1: AdaptiveFragmentFactory
    override val state = arrayOfNulls<Any>(2)

    override var dirtyMask = adaptiveInitStateMask

    @Suppress("UNCHECKED_CAST")
    val iterator
        get() = state[0] as Iterator<IT>

    @Suppress("UNCHECKED_CAST")
    val builder
        get() = state[1] as AdaptiveFragmentFactory<BT>

    override val thisClosure = AdaptiveClosure(arrayOf(this), state.size)

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    val placeholder: AdaptiveBridge<BT> = adapter.createPlaceholder()

    override fun patchDescendant(fragment: AdaptiveFragment<BT>) {
        // anonymous fragments are patched by `patch()` for loops
    }

    fun addAnonymous(iteratorValue: IT): AdaptiveFragment<BT> =
        AdaptiveAnonymous(adapter, this, 0, 1, builder).also {
            fragments.add(it)
            it.state[0] = iteratorValue
            it.create()
        }

    override fun create() {
        if (adapter.trace) trace("create")

        patchExternal()
        // no internal for structural fragments

        for (loopVariable in iterator) {
            addAnonymous(loopVariable)
        }

        dirtyMask = adaptiveCleanStateMask
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("mount", bridge)

        bridge.add(placeholder)

        for (fragment in fragments) {
            fragment.mount(placeholder)
        }
    }

    override fun patchInternal() {
        if (adapter.trace) traceWithState("beforePatchInternal")

        // TODO think about re-running iterators, we should not do that
        if (dirtyMask != 0) {
            patchStructure()
        } else {
            patchContent()
        }

        dirtyMask = adaptiveCleanStateMask

        if (adapter.trace) traceWithState("afterPatchInternal")
    }

    fun patchStructure() {
        var index = 0
        for (loopVariable in iterator) {
            if (index >= fragments.size) {
                val f = addAnonymous(loopVariable)
                f.create()
                f.mount(placeholder)
            } else {
                fragments[index].also {
                    it.patch()
                }
            }
            index ++
        }
        while (index < fragments.size) {
            val f = fragments.removeLast()
            f.unmount(placeholder)
            f.dispose()
            index ++
        }
    }

    fun patchContent() {
        for (fragment in fragments) {
            fragment.patch()
        }
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("unmount", bridge)

        for (fragment in fragments) {
            fragment.unmount(placeholder)
        }
        bridge.remove(placeholder)
    }

    override fun dispose() {
        if (adapter.trace) trace("dispose")

        for (f in fragments) {
            f.dispose()
        }
    }

    override fun traceWithState(point : String) {
        val s0 = state[0]?.let { it::class.simpleName } ?: "null"
        val s1 = state[1]?.toString() ?: "null"
        adapter.trace(this, point, "closureDirtyMask: ${getClosureDirtyMask()} state: [$s0,$s1]")
    }

}