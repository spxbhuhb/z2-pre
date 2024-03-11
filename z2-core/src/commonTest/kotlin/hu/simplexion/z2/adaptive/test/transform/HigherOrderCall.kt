package hu.simplexion.z2.adaptive.test.transform

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.AdaptiveT0
import hu.simplexion.z2.adaptive.testing.AdaptiveT1
import hu.simplexion.z2.adaptive.testing.TestNode

/**
 * fun higherOrderFun(j : Int, @Adaptive paramFun : (p : Int) -> Unit) { // call site 111 (a block)
 *     T0() // call site 222
 *     paramFun(j + 3) // call site 333
 * }
 */
class HigherOrderFun(
    override val adaptiveAdapter: AdaptiveAdapter<TestNode>,
    override val adaptiveClosure: AdaptiveClosure<TestNode>?,
    override val adaptiveParent: AdaptiveFragment<TestNode>?,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    var j: Int,
    val closure: AdaptiveClosure<TestNode>,
    @Adaptive val paramFun: (parent: AdaptiveAnonymous<TestNode>) -> AdaptiveFragment<TestNode>
) : AdaptiveGeneratedFragment<TestNode> {

    val stateSize = 1
    val stateVariableIndex_j = 0

    var adaptiveDirtyMask = AdaptiveStateVariableMask(stateSize)

    fun adaptiveInvalidate0(statVariableIndex: Int) {
        adaptiveDirtyMask.invalidate(statVariableIndex)
    }

    override fun adaptivePatch() {
        containedFragment.adaptiveExternalPatch(containedFragment)
        containedFragment.adaptivePatch()
        adaptiveDirtyMask.clear()
    }

    override val containedFragment = adaptiveBuilder111(this)

    // ----  block site 111 --------

    val callSiteDependencyMask_111
        get() = callSiteDependencyMask222 or callSiteDependencyMask333

    fun adaptiveBuilder111(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> {
        val tmp = AdaptiveSequence(
            adaptiveAdapter,
            parent.adaptiveClosure,
            parent,
        )

        tmp.add(adaptiveBuilder222(tmp))
        tmp.add(adaptiveBuilder333(tmp))

        return tmp
    }

    fun adaptiveExternalPatch111(fragment: AdaptiveFragment<TestNode>) {
        // sequence has no state variables, therefore there is nothing to patch
        // `patch` will call `externalPatch` and `patch` of all items in the sequence
    }

    // ----  T0 call site 222  --------

    val callSiteDependencyMask222: Int
        get() = 0

    fun adaptiveBuilder222(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> {
        return AdaptiveT0(adaptiveAdapter, parent.adaptiveClosure, this, this::adaptiveExternalPatch222)
    }

    fun adaptiveExternalPatch222(fragment: AdaptiveFragment<TestNode>) {
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

    fun adaptiveBuilder333(parent: AdaptiveFragment<TestNode>?): AdaptiveFragment<TestNode> {
        return AdaptiveAnonymous(
            adaptiveAdapter,
            closure,
            parent,
            this::adaptiveExternalPatch333,
            arrayOf(j + 3)
        ).also { paramFun(it) }
    }

    fun adaptiveExternalPatch333(fragment: AdaptiveFragment<TestNode>) {
        if (adaptiveDirtyMask.isClearOf(callSiteDependencyMask333)) return

        fragment as AdaptiveAnonymous<TestNode>

        if (adaptiveDirtyMask.isDirtyOf(stateVariableIndex_j)) {
            fragment.adaptiveState[stateVariableIndex_333_0] = j + 3
            fragment.adaptiveInvalidate(stateVariableIndex_333_0)
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
    override val adaptiveAdapter: AdaptiveAdapter<TestNode>,
    override val adaptiveParent: AdaptiveFragment<TestNode>,
    override val adaptiveClosure: AdaptiveClosure<TestNode>?,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    var i: Int
) : AdaptiveGeneratedFragment<TestNode> {

    val stateVariableIndex_i = 0
    val closureVariableIndex_i = stateVariableIndex_i // for the declaration scope closure variable index is the same as the state variable index

    val scopeSize = 1 // one state variable in this scope
    val closureSize = scopeSize // for the declaration scope these two are the same

    var adaptiveDirtyMask = AdaptiveStateVariableMask(1)

    fun adaptiveInvalidate(stateVariableIndex: Int) {
        adaptiveDirtyMask.invalidate(stateVariableIndex)
    }

    override fun adaptivePatch() {
        containedFragment.adaptiveExternalPatch(containedFragment)
        containedFragment.adaptivePatch()
        adaptiveDirtyMask.clear()
    }

    override val containedFragment = adaptiveBuilder111(this)

    // ----  call site 111 --------
    //
    // higherOrderFun(i*2) {
    //

    val callSiteDependencyMask_111 by lazy {
        AdaptiveStateVariableMask.of(
            AdaptiveStateVariableMask.of(stateVariableIndex_i), // the first parameter of H2 depends on `i`
            callSiteDependencyMask_222 // dependencies of  the anonymous function
        )
    }

    fun adaptiveBuilder111(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> {
        return HigherOrderFun(
            adaptiveAdapter,
            null, // direct calls remove the current closure
            parent,
            this::adaptiveExternalPatch111,
            i * 2,
            AdaptiveClosure(this, emptyArray(), 1),
            this::adaptiveBuilder222
        )
    }

    fun adaptiveExternalPatch111(fragment: AdaptiveFragment<TestNode>) {
        if (adaptiveDirtyMask.isClearOf(callSiteDependencyMask_111)) return

        fragment as HigherOrderFun

        if (adaptiveDirtyMask.isDirtyOf(stateVariableIndex_i)) {
            fragment.j = i * 2
            fragment.adaptiveInvalidate0(fragment.stateVariableIndex_j)
        }
    }

    // ----  Anonymous function site 222 --------

    val scopeIndex_222 = 0 // this is the first anonymous scope in the closure
    val scopeVariableIndex_222_p = 0 // `p` is the first variable in this scope
    val closureVariableIndex_222_p = 1 // `p` is the second variable in this closure, `i` is the first
    val closureSize_222 = 2 // two closure variables: `i` and `p`

    val callSiteDependencyMask_222 by lazy {
        AdaptiveStateVariableMask.of(
            callSiteDependencyMask333, callSiteDependencyMask_444
        )
    }

    fun adaptiveGetValue_222_p(closure: AdaptiveClosure<TestNode>): Int =
        closure.anonymousScopes[scopeIndex_222].adaptiveState[scopeVariableIndex_222_p] as Int

    fun adaptiveBuilder222(parent: AdaptiveAnonymous<TestNode>): AdaptiveFragment<TestNode> {
        val tmp = AdaptiveSequence(
            adaptiveAdapter,
            parent.extendedClosure,
            parent
        )

        tmp.add(adaptiveBuilder333(tmp))
        tmp.add(adaptiveBuilder444(tmp))

        return tmp
    }

    fun adaptiveExternalPatch222(fragment: AdaptiveFragment<TestNode>) {
        fragment as AdaptiveSequence
        fragment.adaptivePatch()
    }

    // ----  T0 call site 333 --------

    val callSiteDependencyMask333 = AdaptiveStateVariableMask(0)

    fun adaptiveBuilder333(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> {
        return AdaptiveT0(adaptiveAdapter, parent.adaptiveClosure, this, this::adaptiveExternalPatch333)
    }

    fun adaptiveExternalPatch333(fragment: AdaptiveFragment<TestNode>) {
        // nothing to do here as there are no parameters
    }

    // ----  T1 call site 444 --------
    //
    //  T1(i + p)
    //

    val callSiteDependencyMask_444 = AdaptiveStateVariableMask.of(
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
    val callParameterDependencyMask_444_0 = AdaptiveStateVariableMask.of(
        closureVariableIndex_i,
        closureVariableIndex_222_p
    )

    /**
     * The `dispatchReceiver` of this builder is the original component instance.
     * The `parent` parameter may vary depending on the actual code, it may be the
     * anonymous component directly or a structural.
     */
    fun adaptiveBuilder444(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> {

        val closure = parent.adaptiveClosure !!

        return AdaptiveT1(
            adaptiveAdapter,
            null, // direct calls remove the current closure
            this,
            this::adaptiveExternalPatch444,
            adaptiveGetValue_222_p(closure) + this.i
        )
    }

    fun adaptiveExternalPatch444(fragment: AdaptiveFragment<TestNode>) {
        val closure = fragment.adaptiveClosure ?: return
        if (closure.isClearOf(callSiteDependencyMask_444)) return

        fragment as AdaptiveT1

        if (closure.isDirtyOf(callParameterDependencyMask_444_0)) {
            fragment.p0 = adaptiveGetValue_222_p(closure) + this.i
            fragment.adaptiveInvalidate0(fragment.stateMask_p0) // this call sets the dirty mask of T1, so patch will update whatever it has to update
        }
    }

}