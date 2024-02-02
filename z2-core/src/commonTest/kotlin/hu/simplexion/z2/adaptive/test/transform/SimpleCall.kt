package hu.simplexion.z2.adaptive.test.transform

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.RuiT1

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
    override val ruiParent: RuiFragment<BT>?,
    override val ruiExternalPatch: RuiExternalPatchType<BT>,
    var i: Int
) : RuiGeneratedFragment<BT> {

    val stateMask_i : RuiStateVariableMask
        get() = RuiStateVariableMask(1)

    var ruiDirty0 = 0L
    // bit 0 represents i

    fun ruiInvalidate0(mask: Long) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch() {
        containedFragment.ruiExternalPatch(containedFragment)
        ruiDirty0 = 0L
    }

    override val containedFragment = ruiBuilder123(this)

    // ----  T1  --------

    val callSiteDependencyMask123
        get() = stateMask_i

    fun ruiBuilder123(parent: RuiFragment<BT>): RuiFragment<BT> {
        return RuiT1(ruiAdapter, null, parent, this::ruiExternalPatch123, i*2)
    }

    fun ruiExternalPatch123(it : RuiFragment<BT>) {
        TODO()
    }
}