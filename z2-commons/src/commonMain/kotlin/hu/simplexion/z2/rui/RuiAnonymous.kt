/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui

class RuiAnonymous<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiScope: RuiFragment<BT>,
    override val ruiExternalPatch: RuiExternalPathType<BT>,
    override val ruiCallSiteDependencyMask: RuiStateVariableMask,
    val state : Array<Any?>,
) : RuiGeneratedFragment<BT> {

    override lateinit var containedFragment: RuiFragment<BT>

    override val ruiStateSize: Int
        get() = state.size

    var ruiDirty0 : RuiStateVariableMask = 0

    @Suppress("unused")
    fun ruiInvalidate0(mask: RuiStateVariableMask) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch(dirtyMaskOfScope: RuiStateVariableMask) {
        val extendedScopeMask = containedFragment.ruiExternalPatch(containedFragment, dirtyMaskOfScope)
        if (extendedScopeMask != 0) containedFragment.ruiPatch(extendedScopeMask)
    }
}