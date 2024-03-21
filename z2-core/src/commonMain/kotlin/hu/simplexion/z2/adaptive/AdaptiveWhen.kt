/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveWhen<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val closure: AdaptiveClosure<BT>,
    override val parent: AdaptiveFragment<BT>,
    override val index: Int
) : AdaptiveStructuralFragment<BT> {

    val placeholder: AdaptiveBridge<BT> = adapter.createPlaceholder()

    /**
     * The state contains one variable which is the branch that should be shown.
     * Updated by [adaptiveExternalPatch]. When it is different than [shownBranch]
     * [adaptiveInternalPatch] replaces the fragment shown and updates [shownBranch].
     */
    override val state: Array<Any?> = arrayOf(
        -1 // the branch to show, changed by external patch
    )

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
    var shownBranch = -1 // -1 means that there is nothing shown, like:

    /**
     * The fragment that is currently shown.
     */
    var shownFragment: AdaptiveFragment<BT>? = null

    override fun adaptiveCreate() {
        if (adapter.trace) adapter.trace("AdaptiveWhen", "create")
        shownFragment?.adaptiveCreate()
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace("AdaptiveWhen", "mount", "bridge:", bridge)
        bridge.add(placeholder)
        shownFragment?.adaptiveMount(placeholder)
    }

    override fun adaptiveExternalPatch() {
        closure.declarationScope.adaptivePatch(this, index) // this will update the branch in the state
    }

    override fun adaptiveInternalPatch() {
        if (adapter.trace) adapter.trace("AdaptiveWhen", "patch", "branch:", shownBranch, "newBranch:", newBranch)

        if (stateBranch != shownBranch) {
            shownBranch = stateBranch
            shownFragment =
        }


        if (newBranch == shownBranch) {
            shownFragment?.let {
                it.adaptiveExternalPatch(it)
                it.adaptiveInternalPatch()
            }
        } else {
            shownFragment?.adaptiveUnmount(placeholder)
            shownFragment?.adaptiveDispose()
            shownFragment = closure.declarationScope.adaptiveBuild(closure, this, shownBranch)
            shownFragment?.adaptiveCreate()
            shownFragment?.adaptiveMount(placeholder)
            shownBranch = stateBranch
        }
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace("AdaptiveWhen", "unmount", "bridge:", bridge)
        shownFragment?.adaptiveUnmount(placeholder)
        bridge.remove(placeholder)
    }

    override fun adaptiveDispose() {
        if (adapter.trace) adapter.trace("AdaptiveWhen", "dispose")
        shownFragment?.adaptiveDispose()
    }

}