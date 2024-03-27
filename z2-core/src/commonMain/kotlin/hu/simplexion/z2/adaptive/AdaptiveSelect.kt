/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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

    override var dirtyMask: Int = 0

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
        if (adapter.trace) adapter.trace("AdaptiveSelect", id, "create")

        createClosure.owner.patch(this)

        if (stateBranch != shownBranch) {
            createClosure.owner.build(this, stateBranch)
            shownBranch = stateBranch
        }
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace("AdaptiveSelect", id, "mount", "bridge:", bridge)
        bridge.add(placeholder)
        shownFragment?.mount(placeholder)
    }

    override fun patch() {
        createClosure.owner.patch(this) // this will update the branch in the state

        if (adapter.trace) adapter.trace("AdaptiveSelect", id, "patch", "shownBranch:", shownBranch, "stateBranch:", stateBranch)

        if (stateBranch == shownBranch) {
            shownFragment?.patch()
        } else {
            shownFragment?.unmount(placeholder)
            shownFragment?.dispose()

            if (stateBranch == -1) {
                shownFragment = null
            } else {
                shownFragment = createClosure.owner.build(this, stateBranch)
                shownFragment?.mount(placeholder)
            }

            shownBranch = stateBranch
        }
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace("AdaptiveSelect", id, "unmount", "bridge:", bridge)
        shownFragment?.unmount(placeholder)
        bridge.remove(placeholder)
    }

    override fun dispose() {
        if (adapter.trace) adapter.trace("AdaptiveSelect", id, "dispose")
        shownFragment?.dispose()
    }

}