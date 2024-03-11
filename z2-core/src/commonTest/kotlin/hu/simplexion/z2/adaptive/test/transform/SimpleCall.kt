package hu.simplexion.z2.adaptive.test.transform

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.AdaptiveT1

/**
 * ```kotlin
 * fun simpleCall(i : Int) {
 *     T1(i*2)
 * }
 * ```
 */
class SimpleCall<BT : AdaptiveFragment<BT>>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveClosure: AdaptiveClosure<BT>?,
    override val adaptiveParent: AdaptiveFragment<BT>?,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT>,
    var i: Int
) : AdaptiveGeneratedFragment<BT> {

    val stateMask_i : AdaptiveStateVariableMask
        get() = AdaptiveStateVariableMask(1)

    var adaptiveDirty0 = 0L
    // bit 0 represents i

    fun adaptiveInvalidate0(mask: Long) {
        adaptiveDirty0 = adaptiveDirty0 or mask
    }

    override fun adaptivePatch() {
        containedFragment.adaptiveExternalPatch(containedFragment)
        adaptiveDirty0 = 0L
    }

    override val containedFragment = adaptiveBuilder123(this)

    // ----  T1  --------

    val callSiteDependencyMask123
        get() = stateMask_i

    fun adaptiveBuilder123(parent: AdaptiveFragment<BT>): AdaptiveFragment<BT> {
        return AdaptiveT1(adaptiveAdapter, null, parent, this::adaptiveExternalPatch123, i*2)
    }

    fun adaptiveExternalPatch123(it : AdaptiveFragment<BT>) {
        TODO()
    }
}