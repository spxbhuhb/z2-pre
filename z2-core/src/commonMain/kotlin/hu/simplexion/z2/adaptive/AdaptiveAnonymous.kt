/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveAnonymous<BT>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveClosure: AdaptiveClosure<BT>,
    override val adaptiveParent: AdaptiveFragment<BT>?,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT>,
    override val adaptiveState: Array<Any?>
) : AdaptiveGeneratedFragment<BT> {

    override lateinit var containedFragment: AdaptiveFragment<BT>

    val extendedClosure = adaptiveClosure.extendWith(this)

    /**
     * Invalidate a state variable of this *anonymous component instance*.
     *
     * @param  stateVariableIndex  The index of the state variable in [adaptiveState]. It shall not take
     *                             [adaptiveClosure] into account, it is relative to `this`.
     */
    fun adaptiveInvalidate(stateVariableIndex: Int) {
        extendedClosure.invalidate(adaptiveClosure.closureSize + stateVariableIndex)
    }

    override fun adaptivePatch() {
        extendedClosure.copyFrom(adaptiveClosure)
        containedFragment.adaptiveExternalPatch(containedFragment)
        containedFragment.adaptivePatch()
        extendedClosure.clear()
    }

}