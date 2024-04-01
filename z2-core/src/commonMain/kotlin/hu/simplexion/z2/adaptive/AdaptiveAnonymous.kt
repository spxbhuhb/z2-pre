/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveAnonymous<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>,
    index: Int,
    stateSize: Int,
    val factory: AdaptiveFragmentFactory<BT>,
) : AdaptiveGeneratedFragment<BT>(adapter, parent, index, stateSize) {

    override val createClosure : AdaptiveClosure<BT>
        get() = parent!!.thisClosure

    override val thisClosure = extendWith(this, factory.declaringFragment)

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? {
        shouldNotRun()
    }

    override fun patchDescendant(fragment: AdaptiveFragment<BT>) {
        factory.declaringFragment.patchDescendant(fragment)
    }

    override fun invoke(supportFunction: AdaptiveSupportFunction<BT>, vararg arguments: Any?) {
        shouldNotRun()
    }

    override fun create() {
        if (adapter.trace) adapter.trace(this, "create", "")
        patchExternal()
        containedFragment = factory.build(this)
    }

    /**
     * Finds the first parent with `thisClosure` owned by [declaringComponent]. Then extends that closure with
     * the component and returns with the extended closure.
     *
     * Anonymous components use this function to find their declaring closure and extend it with themselves.
     */
    fun extendWith(component: AdaptiveFragment<BT>, declaringComponent: AdaptiveFragment<BT>): AdaptiveClosure<BT> {
        var ancestor = component.parent

        while (ancestor != null && ancestor.thisClosure.owner !== declaringComponent) {
            ancestor = ancestor.parent
        }

        checkNotNull(ancestor) { "couldn't find declaring component for closure extension" }

        val declaringClosure = ancestor.thisClosure

        return AdaptiveClosure(
            declaringClosure.components + component,
            declaringClosure.closureSize + component.state.size
        )
    }

}