/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveAnonymous<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val closure: AdaptiveClosure<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT>,
    override val state: Array<Any?>
) : AdaptiveGeneratedFragment<BT> {

    override lateinit var containedFragment: AdaptiveFragment<BT>

    val extendedClosure = closure.extendWith(this)

    /**
     * Invalidate a state variable of this *anonymous component instance*.
     *
     * @param  stateVariableIndex  The index of the state variable in [state]. It shall not take
     *                             [adaptiveClosure] into account, it is relative to `this`.
     */
    fun adaptiveInvalidate(stateVariableIndex: Int) {
        extendedClosure.invalidate(closure.closureSize + stateVariableIndex)
    }

    override fun adaptiveInternalPatch() {
        extendedClosure.copyFrom(closure)
        containedFragment.adaptiveExternalPatch(containedFragment)
        containedFragment.adaptiveInternalPatch()
        extendedClosure.clear()
    }

}