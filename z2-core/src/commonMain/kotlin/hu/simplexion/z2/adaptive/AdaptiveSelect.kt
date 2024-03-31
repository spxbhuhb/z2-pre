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

    override val state = arrayOf<Any?>(null, -1)

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
    var shownBranch // -1 means that there is nothing shown, like:
        get() = state[1] as Int
        set(v) { state[1] = v }

    var mounted = false

    /**
     * The fragment that is currently shown.
     */
    var shownFragment: AdaptiveFragment<BT>? = null

    override fun create() {
        if (adapter.trace) trace("create")

        patch()

        dirtyMask = adaptiveCleanStateMask
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("mount", bridge)
        mounted = true
        bridge.add(placeholder)
        shownFragment?.mount(placeholder)
    }

    override fun patchInternal() {
        if (adapter.trace) traceWithState("beforePatchInternal")

        if (stateBranch == shownBranch) {
            shownFragment?.patch()
        } else {
            if (mounted) shownFragment?.unmount(placeholder)
            shownFragment?.dispose()

            if (stateBranch == - 1) {
                shownFragment = null
            } else {
                shownFragment = createClosure.owner.build(this, stateBranch)
                if (mounted) shownFragment?.mount(placeholder)
            }

            shownBranch = stateBranch
        }

        dirtyMask = adaptiveCleanStateMask

        if (adapter.trace) traceWithState("afterPatchInternal")
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        shownFragment?.unmount(placeholder)
        bridge.remove(placeholder)
        mounted = false
        if (adapter.trace) trace("unmount", bridge)
    }

    override fun dispose() {
        if (adapter.trace) trace("dispose")
        shownFragment?.dispose()
    }

}