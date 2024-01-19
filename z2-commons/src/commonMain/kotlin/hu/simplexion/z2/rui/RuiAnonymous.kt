/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui

class RuiAnonymous<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiClosure: RuiClosure<BT>,
    override val ruiParent: RuiFragment<BT>?,
    override val ruiExternalPatch: RuiExternalPathType<BT>,
    val ruiState: Array<Any?>,
) : RuiGeneratedFragment<BT> {

    override lateinit var containedFragment: RuiFragment<BT>

    val extendedClosure = ruiClosure.extendWith(this)

    /**
     * Invalidate a state variable of this *anonymous component instance*.
     *
     * @param  stateVariableIndex  The index of the state variable in [ruiState]. It shall not take
     *                             [ruiClosure] into account, it is relative to `this`.
     */
    fun ruiInvalidate(stateVariableIndex: Int) {
        extendedClosure.invalidate(ruiClosure.closureSize + stateVariableIndex)
    }

    override fun ruiPatch() {
        extendedClosure.copyFrom(ruiClosure)
        containedFragment.ruiExternalPatch(containedFragment)
        containedFragment.ruiPatch()
        extendedClosure.clear()
    }

}