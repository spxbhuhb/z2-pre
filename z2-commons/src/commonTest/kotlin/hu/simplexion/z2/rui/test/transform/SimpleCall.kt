package hu.simplexion.z2.rui.test.transform

import hu.simplexion.z2.rui.*
import hu.simplexion.z2.rui.testing.RuiT1

/**
 * ```kotlin
 * fun simpleCall(i : Int) {
 *     T1(i*2)
 * }
 * ```
 */
class SimpleCall<BT : RuiFragment<BT>>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiClosure: RuiClosure<BT>?,
    override val ruiExternalPatch: RuiExternalPathType<BT>,
    override val ruiCallSiteDependencyMask: RuiStateVariableMask,
    var i: Int
) : RuiGeneratedFragment<BT> {

    val stateMask_i : RuiStateVariableMask
        get() = 1

    override val ruiStateSize: Int
        get() = 1

    var ruiDirty0 = 0L
    // bit 0 represents i

    fun ruiInvalidate0(mask: Long) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch(dirtyMaskOfScope: RuiStateVariableMask) {
        val extendedScopeMask = containedFragment.ruiExternalPatch(containedFragment, dirtyMaskOfScope)
        if (extendedScopeMask != 0) containedFragment.ruiPatch(extendedScopeMask)
        ruiDirty0 = 0L
    }

    override val containedFragment = ruiBuilder123(this)

    // ----  T1  --------

    val callSiteDependencyMask123
        get() = stateMask_i

    fun ruiBuilder123(parentScope: RuiFragment<BT>): RuiFragment<BT> {
        return RuiT1(ruiAdapter, parentScope, this::ruiExternalPatch123, callSiteDependencyMask123, i*2)
    }

    fun ruiExternalPatch123(it : RuiFragment<BT>, scopeDirtyMask : RuiStateVariableMask) : RuiStateVariableMask {
        if (scopeDirtyMask.isClearOf(it.ruiCallSiteDependencyMask)) return CLEAR_STATE_MASK

        it as RuiT1

        if (scopeDirtyMask.isDirtyOf(stateMask_i)) {
            it.p0 = this.i * 2
            it.ruiInvalidate0(stateMask_i)
        }

        // FIXME wrong extend of scopeDirtyMask
        return scopeDirtyMask.extendWith(it.ruiDirty0, it.ruiStateSize)
    }
}