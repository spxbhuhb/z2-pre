/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui

class RuiAnonymous<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiParent: RuiFragment<BT>?,
    override val ruiScope: RuiFragment<BT>,
    override val ruiExternalPatch: RuiExternalPathType<BT>,
    override val ruiCallSiteDependencyMask: RuiStateVariableMask,
    val ruiState: Array<Any?>,
    val ruiScopeSize: Int
) : RuiGeneratedFragment<BT> {

    override lateinit var containedFragment: RuiFragment<BT>

    override val ruiStateSize: Int
        get() = ruiState.size

    /**
     * Components that store state variables of this scope.
     */
    val ruiScopeComponents = collectStateComponents()

    var ruiDirty0: RuiStateVariableMask = 0

    @Suppress("unused")
    fun ruiInvalidate0(mask: RuiStateVariableMask) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch(dirtyMaskOfScope: RuiStateVariableMask) {
        val extendedScopeMask = containedFragment.ruiExternalPatch(containedFragment, dirtyMaskOfScope)
        if (extendedScopeMask != 0) containedFragment.ruiPatch(extendedScopeMask)
    }

    /**
     * Traverses up the Rui fragment tree and collects all fragments that store state variables
     * of this scope. The returned array is used by [ruiPatch] when updating [containedFragment].
     */
    fun collectStateComponents(): Array<RuiFragment<BT>> {
        var current = ruiParent
        val result = arrayOfNulls<RuiFragment<BT>?>(ruiScopeSize)
        var index = ruiScopeSize - 1

        while (current != null && index > 0) {
            if (current === ruiScope) { // the start scope, this should be the first element of the array
                result[index] = current
                break
            }
            if (current.ruiScope == ruiScope) {
                result[index] = current
                index --
            }
            current = current.ruiParent
        }

        check(index == 0) { "invalid Rui tree" }

        @Suppress("UNCHECKED_CAST")
        return result as Array<RuiFragment<BT>>
    }
}