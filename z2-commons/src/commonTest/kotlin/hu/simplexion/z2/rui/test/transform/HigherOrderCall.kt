package hu.simplexion.z2.rui.test.transform

import hu.simplexion.z2.rui.*
import hu.simplexion.z2.rui.testing.RuiT0
import hu.simplexion.z2.rui.testing.RuiT1
import hu.simplexion.z2.rui.testing.TestNode

/**
 * fun higherOrderFun(j : Int, @Rui paramFun : (p : Int) -> Unit) { // call site 111 (a block)
 *     T0() // call site 222
 *     paramFun(j + 3) // call site 333
 * }
 */
class HigherOrderFun(
    override val ruiAdapter: RuiAdapter<TestNode>,
    override val ruiParent: RuiFragment<TestNode>,
    override val ruiScope: RuiFragment<TestNode>,
    override val ruiExternalPatch: RuiExternalPathType<TestNode>,
    override val ruiCallSiteDependencyMask: RuiStateVariableMask,
    var j: Int,
    @Rui val paramFun: (parent: RuiAnonymous<TestNode>) -> RuiFragment<TestNode>
) : RuiGeneratedFragment<TestNode> {

    val stateMask_j: RuiStateVariableMask = 0b1
    val stateIndex_j = 0

    override val ruiStateSize = 1

    var ruiDirty0: RuiStateVariableMask = 0

    fun ruiInvalidate0(mask: RuiStateVariableMask) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch(dirtyMaskOfScope: RuiStateVariableMask) {
        val extendedScopeMask = containedFragment.ruiExternalPatch(containedFragment, dirtyMaskOfScope)
        if (extendedScopeMask != 0) containedFragment.ruiPatch(extendedScopeMask)
        ruiDirty0 = 0
    }

    override val containedFragment = ruiBuilder111(this)

    // ----  block site 111 --------

    val callSiteDependencyMask_111
        get() = callSiteDependencyMask222 or callSiteDependencyMask333

    fun ruiBuilder111(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        return RuiSequence(
            ruiAdapter,
            parent,
            callSiteDependencyMask_111,
            this::ruiBuilder222,
            this::ruiBuilder333
        )
    }

    fun ruiExternalPatch111(it: RuiFragment<TestNode>, scopeDirtyMask: RuiStateVariableMask): RuiStateVariableMask {
        it as RuiSequence
        it.ruiPatch(scopeDirtyMask)
        return CLEAR_STATE_MASK // FIXME
    }

    // ----  T0 call site 222  --------

    val callSiteDependencyMask222: Int
        get() = 0

    fun ruiBuilder222(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        return RuiT0(ruiAdapter, parent, this, this::ruiExternalPatch333, callSiteDependencyMask333)
    }

    fun ruiExternalPatch222(it: RuiFragment<TestNode>, scopeDirtyMask: RuiStateVariableMask): RuiStateVariableMask {
        return CLEAR_STATE_MASK
    }

    // ----  paramFun call site 333 --------

    val callSiteDependencyMask333: Int
        get() = stateMask_j

    val stateMask_p: RuiStateVariableMask = 0b1
    val stateIndex_p: RuiStateVariableMask = 0

    fun ruiBuilder333(parent: RuiFragment<TestNode>?): RuiFragment<TestNode> {
        return RuiAnonymous(
            ruiAdapter,
            parent,
            this::ruiExternalPatch333,
            0,
            arrayOf(j + 3)
        ).also { paramFun(it) }
    }

    fun ruiExternalPatch333(it: RuiFragment<TestNode>, scopeDirtyMask: RuiStateVariableMask): RuiStateVariableMask {
        if (scopeDirtyMask.isClearOf(it.ruiCallSiteDependencyMask)) return CLEAR_STATE_MASK

        it as RuiAnonymous<TestNode>

        if (scopeDirtyMask.isDirtyOf(stateMask_j)) {
            it.ruiState[stateIndex_p] = j + 3
            it.ruiInvalidate0(stateIndex_p)
        }

        // FIXME rui state mask extend
        return scopeDirtyMask.extend(it.ruiDirty0, it.ruiStateSize)
    }
}

/**
 * ```kotlin
 * fun higherOrderCall(i : Int) {
 *     higherOrderFun(i*2) { p ->  // call site 111 (for higherOrderFun) and site 222 (for the parameter function)
 *         T0()      // call site 333
 *         T1(i + p) // call site 444
 *     }
 * }
 * ```
 */
class HigherOrderCall(
    override val ruiAdapter: RuiAdapter<TestNode>,
    override val ruiParent: RuiFragment<TestNode>,
    override val ruiScope: RuiFragment<TestNode>,
    override val ruiExternalPatch: RuiExternalPathType<TestNode>,
    override val ruiCallSiteDependencyMask: RuiStateVariableMask,
    var i: Int
) : RuiGeneratedFragment<TestNode> {

    val stateMask_i: RuiStateVariableMask = 0b1
    val stateIndex_i = 0

    override val ruiStateSize = 1

    var ruiDirty0: RuiStateVariableMask = 0

    fun ruiInvalidate0(mask: RuiStateVariableMask) {
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch(dirtyMaskOfScope: RuiStateVariableMask) {
        val extendedScopeMask = containedFragment.ruiExternalPatch(containedFragment, dirtyMaskOfScope)
        if (extendedScopeMask != 0) containedFragment.ruiPatch(extendedScopeMask)
        ruiDirty0 = 0
    }

    override val containedFragment = ruiBuilder111(this)

    // ----  H2 call site 111 --------

    val callSiteDependencyMask_111: RuiStateVariableMask by lazy {
        stateMask_i or callSiteDependencyMask_222
    }

    fun ruiBuilder111(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        return HigherOrderFun(ruiAdapter, parent, this, this::ruiExternalPatch111, callSiteDependencyMask_111, i * 2, this::ruiBuilder222)
    }

    fun ruiExternalPatch111(it: RuiFragment<TestNode>, scopeDirtyMask: RuiStateVariableMask): RuiStateVariableMask {
        if (scopeDirtyMask.isClearOf(it.ruiCallSiteDependencyMask)) return 0

        it as HigherOrderFun

        if (scopeDirtyMask.isDirtyOf(stateMask_i)) {
            it.j = i * 2
            it.ruiInvalidate0(it.stateMask_j)
        }

        // FIXME rui state mask extend
        return scopeDirtyMask.extend(it.ruiDirty0, it.ruiStateSize)
    }

    // ----  Anonymous function site 222 --------

    val stateMask_222_p: RuiStateVariableMask = 0b10

    val stateIndex_222_p = 0

    val stateSize_222 = 1

    val callSiteDependencyMask_222: RuiStateVariableMask = callSiteDependencyMask333 or callSiteDependencyMask444

    fun ruiBuilder222(parent: RuiAnonymous<TestNode>): RuiFragment<TestNode> {
        return RuiSequence(
            ruiAdapter,
            parent,
            callSiteDependencyMask_222,
            this::ruiBuilder333,
            this::ruiBuilder444
        )
    }

    fun ruiExternalPatch222(it: RuiFragment<TestNode>, scopeDirtyMask: RuiStateVariableMask): RuiStateVariableMask {
        it as RuiSequence
        it.ruiPatch(scopeDirtyMask)
        return CLEAR_STATE_MASK // FIXME
    }

    // ----  T0 call site 333 --------

    val callSiteDependencyMask333: Int
        get() = 0

    fun ruiBuilder333(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        return RuiT0(ruiAdapter, parent, this, this::ruiExternalPatch333, callSiteDependencyMask333)
    }

    fun ruiExternalPatch333(it: RuiFragment<TestNode>, scopeDirtyMask: RuiStateVariableMask): RuiStateVariableMask {
        return CLEAR_STATE_MASK
    }

    // ----  T1 call site 444 --------

    val callSiteDependencyMask444: Int
        get() = stateMask_i or stateMask_222_p

    /**
     * The `dispatchReceiver` of this builder is the original component.
     * The `parentScope` of this function will be an instance of `RuiAnonymous` built by `ruiBuilder222`.
     *
     * - `3` is the call site dependency mask
     * - the state variables of this scope are:
     *    - `i` from the start scope
     *    - `p` from the anonymous fragment
     * - call site bits:
     *    - bit 0 is `i`
     *    - bit 1 is `p`
     *
     * T1 depends both on `i` from the start scope and `p` from the anonymous function scope.
     */
    fun ruiBuilder444(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        val p1 = parent as RuiAnonymous<TestNode>
        return RuiT1(ruiAdapter, parent, this, this::ruiExternalPatch444, callSiteDependencyMask444, (p1.ruiState[stateIndex_222_p] as Int) + this.i)
    }

    fun ruiExternalPatch444(it: RuiFragment<TestNode>, scopeDirtyMask: RuiStateVariableMask): RuiStateVariableMask {
        if (scopeDirtyMask.isClearOf(it.ruiCallSiteDependencyMask)) return CLEAR_STATE_MASK

        val p1 = it.ruiScope as RuiAnonymous<TestNode>
        it as RuiT1

        if (scopeDirtyMask.isDirtyOf(stateMask_i or stateMask_222_p)) {
            it.p0 = (p1.ruiState[stateIndex_222_p] as Int) + this.i
            it.ruiInvalidate0(it.stateMask_p0)
        }

        // FIXME rui state mask extend
        return scopeDirtyMask.extend(it.ruiDirty0, it.ruiStateSize)
    }

}