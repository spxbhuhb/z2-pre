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

    override val thisClosure = createClosure.extendWith(this, factory.declaringFragment)

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
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

}