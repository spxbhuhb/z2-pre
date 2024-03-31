/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

interface AdaptiveStructuralFragment<BT> : AdaptiveFragment<BT> {

    override val createClosure : AdaptiveClosure<BT>
        get() = parent!!.thisClosure

    override val thisClosure
        get() = createClosure

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        shouldNotRun()
    }

    override fun patchDescendant(fragment: AdaptiveFragment<BT>) {
        shouldNotRun()
    }

    override fun invoke(supportFunction: AdaptiveSupportFunction<BT>, vararg arguments: Any?) {
        shouldNotRun()
    }

    override fun patchExternal() {
        createClosure.owner.patchDescendant(this)
        if (adapter.trace) traceWithState("patchExternal")
    }
}