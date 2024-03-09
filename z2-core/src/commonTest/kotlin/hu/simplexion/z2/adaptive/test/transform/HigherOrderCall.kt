package hu.simplexion.z2.adaptive.test.transform

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.RuiT0
import hu.simplexion.z2.adaptive.testing.RuiT1
import hu.simplexion.z2.adaptive.testing.TestNode

/**
 * fun higherOrderFun(j : Int, @Rui paramFun : (p : Int) -> Unit) { // call site 111 (a block)
 *     T0() // call site 222
 *     paramFun(j + 3) // call site 333
 * }
 */
class HigherOrderFun(
    override val ruiAdapter: RuiAdapter<TestNode>,
    override val ruiClosure: RuiClosure<TestNode>?,
    override val ruiParent: RuiFragment<TestNode>?,
    override val ruiExternalPatch: RuiExternalPatchType<TestNode>,
    var j: Int,
    val closure: RuiClosure<TestNode>,
    @Rui val paramFun: (parent: RuiAnonymous<TestNode>) -> RuiFragment<TestNode>
) : RuiGeneratedFragment<TestNode> {

    val stateSize = 1
    val stateVariableIndex_j = 0

    var ruiDirtyMask = RuiStateVariableMask(stateSize)

    fun ruiInvalidate0(statVariableIndex: Int) {
        ruiDirtyMask.invalidate(statVariableIndex)
    }

    override fun ruiPatch() {
        containedFragment.ruiExternalPatch(containedFragment)
        containedFragment.ruiPatch()
        ruiDirtyMask.clear()
    }

    override val containedFragment = ruiBuilder111(this)

    // ----  block site 111 --------

    val callSiteDependencyMask_111
        get() = callSiteDependencyMask222 or callSiteDependencyMask333

    fun ruiBuilder111(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        return RuiSequence(
            ruiAdapter,
            parent.ruiClosure,
            parent,
            this::ruiBuilder222,
            this::ruiBuilder333
        )
    }

    fun ruiExternalPatch111(fragment: RuiFragment<TestNode>) {
        // sequence has no state variables, therefore there is nothing to patch
        // `patch` will call `externalPatch` and `patch` of all items in the sequence
    }

    // ----  T0 call site 222  --------

    val callSiteDependencyMask222: Int
        get() = 0

    fun ruiBuilder222(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        return RuiT0(ruiAdapter, parent.ruiClosure, this, this::ruiExternalPatch222)
    }

    fun ruiExternalPatch222(fragment: RuiFragment<TestNode>) {
        //T0  has no state variables, therefore there is nothing to patch
    }

    // ----  paramFun call site 333 --------
    //
    // paramFun(j + 3)
    //

    // state variable index of the first parameter of the anonymous component
    val stateVariableIndex_333_0: Int
        get() = 0

    val callSiteDependencyMask333: Int
        get() = stateVariableIndex_j

    // index of the first state variable in the anonymous component
    val stateVariableIndex_333_1 = 0

    fun ruiBuilder333(parent: RuiFragment<TestNode>?): RuiFragment<TestNode> {
        return RuiAnonymous(
            ruiAdapter,
            closure,
            parent,
            this::ruiExternalPatch333,
            arrayOf(j + 3)
        ).also { paramFun(it) }
    }

    fun ruiExternalPatch333(fragment: RuiFragment<TestNode>) {
        if (ruiDirtyMask.isClearOf(callSiteDependencyMask333)) return

        fragment as RuiAnonymous<TestNode>

        if (ruiDirtyMask.isDirtyOf(stateVariableIndex_j)) {
            fragment.ruiState[stateVariableIndex_333_0] = j + 3
            fragment.ruiInvalidate(stateVariableIndex_333_0)
        }
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
    override val ruiClosure: RuiClosure<TestNode>?,
    override val ruiExternalPatch: RuiExternalPatchType<TestNode>,
    var i: Int
) : RuiGeneratedFragment<TestNode> {

    val stateVariableIndex_i = 0
    val closureVariableIndex_i = stateVariableIndex_i // for the declaration scope closure variable index is the same as the state variable index

    val scopeSize = 1 // one state variable in this scope
    val closureSize = scopeSize // for the declaration scope these two are the same

    var ruiDirtyMask = RuiStateVariableMask(1)

    fun ruiInvalidate(stateVariableIndex: Int) {
        ruiDirtyMask.invalidate(stateVariableIndex)
    }

    override fun ruiPatch() {
        containedFragment.ruiExternalPatch(containedFragment)
        containedFragment.ruiPatch()
        ruiDirtyMask.clear()
    }

    override val containedFragment = ruiBuilder111(this)

    // ----  call site 111 --------
    //
    // higherOrderFun(i*2) {
    //

    val callSiteDependencyMask_111 by lazy {
        RuiStateVariableMask.of(
            RuiStateVariableMask.of(stateVariableIndex_i), // the first parameter of H2 depends on `i`
            callSiteDependencyMask_222 // dependencies of  the anonymous function
        )
    }

    fun ruiBuilder111(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        return HigherOrderFun(
            ruiAdapter,
            null, // direct calls remove the current closure
            parent,
            this::ruiExternalPatch111,
            i * 2,
            RuiClosure(this, emptyArray(), 1),
            this::ruiBuilder222
        )
    }

    fun ruiExternalPatch111(fragment: RuiFragment<TestNode>) {
        if (ruiDirtyMask.isClearOf(callSiteDependencyMask_111)) return

        fragment as HigherOrderFun

        if (ruiDirtyMask.isDirtyOf(stateVariableIndex_i)) {
            fragment.j = i * 2
            fragment.ruiInvalidate0(fragment.stateVariableIndex_j)
        }
    }

    // ----  Anonymous function site 222 --------

    val scopeIndex_222 = 0 // this is the first anonymous scope in the closure
    val scopeVariableIndex_222_p = 0 // `p` is the first variable in this scope
    val closureVariableIndex_222_p = 1 // `p` is the second variable in this closure, `i` is the first
    val closureSize_222 = 2 // two closure variables: `i` and `p`

    val callSiteDependencyMask_222 by lazy {
        RuiStateVariableMask.of(
            callSiteDependencyMask333, callSiteDependencyMask_444
        )
    }

    fun ruiGetValue_222_p(closure: RuiClosure<TestNode>): Int =
        closure.anonymousScopes[scopeIndex_222].ruiState[scopeVariableIndex_222_p] as Int

    fun ruiBuilder222(parent: RuiAnonymous<TestNode>): RuiFragment<TestNode> {
        return RuiSequence(
            ruiAdapter,
            parent.extendedClosure,
            parent,
            this::ruiBuilder333,
            this::ruiBuilder444
        )
    }

    fun ruiExternalPatch222(fragment: RuiFragment<TestNode>) {
        fragment as RuiSequence
        fragment.ruiPatch()
    }

    // ----  T0 call site 333 --------

    val callSiteDependencyMask333 = RuiStateVariableMask(0)

    fun ruiBuilder333(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {
        return RuiT0(ruiAdapter, parent.ruiClosure, this, this::ruiExternalPatch333)
    }

    fun ruiExternalPatch333(fragment: RuiFragment<TestNode>) {
        // nothing to do here as there are no parameters
    }

    // ----  T1 call site 444 --------
    //
    //  T1(i + p)
    //

    val callSiteDependencyMask_444 = RuiStateVariableMask.of(
        closureVariableIndex_i,
        closureVariableIndex_222_p
    )

    /**
     * Dependency mask of the first parameter of the call. When one of the closure variables change
     * external patch has to update the component state.
     *
     * In this specific case it is the same as the call site dependency mask but in general it is
     * a subset of the call site dependency mask.
     */
    val callParameterDependencyMask_444_0 = RuiStateVariableMask.of(
        closureVariableIndex_i,
        closureVariableIndex_222_p
    )

    /**
     * The `dispatchReceiver` of this builder is the original component instance.
     * The `parent` parameter may vary depending on the actual code, it may be the
     * anonymous component directly or a structural.
     */
    fun ruiBuilder444(parent: RuiFragment<TestNode>): RuiFragment<TestNode> {

        val closure = parent.ruiClosure !!

        return RuiT1(
            ruiAdapter,
            null, // direct calls remove the current closure
            this,
            this::ruiExternalPatch444,
            ruiGetValue_222_p(closure) + this.i
        )
    }

    fun ruiExternalPatch444(fragment: RuiFragment<TestNode>) {
        val closure = fragment.ruiClosure ?: return
        if (closure.isClearOf(callSiteDependencyMask_444)) return

        fragment as RuiT1

        if (closure.isDirtyOf(callParameterDependencyMask_444_0)) {
            fragment.p0 = ruiGetValue_222_p(closure) + this.i
            fragment.ruiInvalidate0(fragment.stateMask_p0) // this call sets the dirty mask of T1, so patch will update whatever it has to update
        }
    }

}