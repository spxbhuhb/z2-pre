/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveSelect<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val index: Int,
) : AdaptiveStructuralFragment<BT> {

    override val id = adapter.newId()

    val placeholder: AdaptiveBridge<BT> = adapter.createPlaceholder()

    // 0: The branch to show
    override val state = arrayOfNulls<Any>(1)

    override var dirtyMask = adaptiveInitStateMask

    val stateBranch
        get() = state[0] as Int

    /**
     * The branch currently shown on the UI, a statement index in the
     * declaring component. -1 means that there is nothing shown,
     * like in the following code when `condition` is false.
     *
     * ```kotlin
     *     if (condition) div {  }
     * ```
     */
    var shownBranch = - 1 // -1 means that there is nothing shown, like:

    /**
     * The fragment that is currently shown.
     */
    var shownFragment: AdaptiveFragment<BT>? = null

    override fun create() {
        if (adapter.trace) trace("create")

        patchExternal()
        // no internals for structural fragments

        if (stateBranch != shownBranch) {
            createClosure.owner.build(this, stateBranch)
            shownBranch = stateBranch
        }

        dirtyMask = adaptiveCleanStateMask
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("mount", bridge)
        bridge.add(placeholder)
        shownFragment?.mount(placeholder)
    }

    override fun patchInternal() {
        patchExternal() // this will update the branch in the state

        if (adapter.trace) traceWithState("patchInternal")

        if (stateBranch == shownBranch) {
            shownFragment?.patchInternal()
        } else {
            shownFragment?.unmount(placeholder)
            shownFragment?.dispose()

            if (stateBranch == - 1) {
                shownFragment = null
            } else {
                shownFragment = createClosure.owner.build(this, stateBranch)
                shownFragment?.mount(placeholder)
            }

            shownBranch = stateBranch
        }

        dirtyMask = adaptiveCleanStateMask
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("unmount", bridge)
        shownFragment?.unmount(placeholder)
        bridge.remove(placeholder)
    }

    override fun dispose() {
        if (adapter.trace) trace("dispose")
        shownFragment?.dispose()
    }

}