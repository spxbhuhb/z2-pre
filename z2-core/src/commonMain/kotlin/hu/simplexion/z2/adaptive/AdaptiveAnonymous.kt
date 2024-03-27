/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveAnonymous<BT>(
    adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>,
    override val index: Int,
    val factory: AdaptiveFragmentFactory<BT>,
    stateSize: Int,
) : AdaptiveGeneratedFragment<BT>(adapter, stateSize) {

    override val createClosure : AdaptiveClosure<BT>
        get() = parent.thisClosure

    override val thisClosure = createClosure.extendWith(this, factory.declaringFragment)

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        shouldNotRun()
    }

    override fun patch(fragment: AdaptiveFragment<BT>) {
        factory.declaringFragment.patch(fragment)
    }

    override fun invoke(supportFunction: AdaptiveSupportFunction<BT>, vararg arguments: Any?) {
        shouldNotRun()
    }

    override fun create() {
        if (adapter.trace) adapter.trace("AdaptiveAnonymous", id, "create")
        createClosure.owner.patch(this)
        containedFragment = factory.build(this)
    }

}