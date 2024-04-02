/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

abstract class AdaptiveFragment<BT>(
    val adapter: AdaptiveAdapter<BT>,
    val parent: AdaptiveFragment<BT>?,
    val index: Int,
    stateSize: Int
) {

    val id: Long = adapter.newId()

    open val createClosure: AdaptiveClosure<BT>?
        get() = parent?.thisClosure

    val state: Array<Any?> = arrayOfNulls<Any?>(stateSize)

    @Suppress("LeakingThis") // closure won't do anything with components during init
    open val thisClosure: AdaptiveClosure<BT> = AdaptiveClosure(arrayOf(this), stateSize)

    var dirtyMask: AdaptiveStateVariableMask = adaptiveInitStateMask

    var containedFragment: AdaptiveFragment<BT>? = null

    // --------------------------------------------------------------------------
    // Functions that support the descendants of this fragment
    // --------------------------------------------------------------------------

    open fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        shouldNotRun()
    }

    open fun patchDescendant(fragment: AdaptiveFragment<BT>) {
        shouldNotRun()
    }

    open fun invoke(supportFunction: AdaptiveSupportFunction<BT>, arguments: Array<out Any?>): Any? {
        if (adapter.trace) traceSupport("beforeInvoke", supportFunction, arguments)

        val result = generatedInvoke(supportFunction, arguments)

        if (supportFunction.fragment.thisClosure.closureDirtyMask() != adaptiveCleanStateMask) {
            supportFunction.fragment.patchInternal()
        }

        if (adapter.trace) traceSupport("afterInvoke", supportFunction, result)
        return result
    }

    open fun generatedInvoke(supportFunction: AdaptiveSupportFunction<BT>, arguments: Array<out Any?>): Any? {
        shouldNotRun()
    }

    // --------------------------------------------------------------------------
    // Functions that operate on the fragment itself
    // --------------------------------------------------------------------------

    open fun create() {
        if (adapter.trace) trace("create")

        patch()

        containedFragment = build(this, 0)

        dirtyMask = adaptiveCleanStateMask
    }

    open fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("mount", bridge)
        containedFragment?.mount(bridge)
    }

    open fun patch() {
        patchExternal()
        patchInternal()
    }

    open fun patchExternal() {
        if (adapter.trace) traceWithState("beforePatchExternal")

        createClosure?.owner?.patchDescendant(this)

        if (adapter.trace) traceWithState("afterPatchExternal")
    }

    open fun patchInternal() {
        if (adapter.trace) traceWithState("beforePatchInternal")

        generatedPatchInternal()

        containedFragment?.patch()

        dirtyMask = adaptiveCleanStateMask

        if (adapter.trace) traceWithState("afterPatchInternal")
    }

    open fun generatedPatchInternal() {
        shouldNotRun()
    }

    open fun unmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("unmount", bridge)
        containedFragment?.unmount(bridge)
    }

    open fun dispose() {
        if (adapter.trace) trace("dispose")
        containedFragment?.dispose()
    }

    // --------------------------------------------------------------------------
    // State and closure functions
    // --------------------------------------------------------------------------

    fun haveToPatch(closureDirtyMask: AdaptiveStateVariableMask, dependencyMask: AdaptiveStateVariableMask): Boolean =
        (dirtyMask == adaptiveInitStateMask) || (closureDirtyMask and dependencyMask) != adaptiveCleanStateMask

    fun getThisClosureDirtyMask(): AdaptiveStateVariableMask =
        thisClosure.closureDirtyMask()

    fun getCreateClosureDirtyMask(): AdaptiveStateVariableMask =
        createClosure?.closureDirtyMask() ?: adaptiveCleanStateMask

    fun getCreateClosureVariable(variableIndex: Int): Any? =
        createClosure?.get(variableIndex)

    fun getThisClosureVariable(variableIndex: Int): Any? =
        thisClosure.get(variableIndex)

    fun setStateVariable(index: Int, value: Any?) {
        state[index] = value
        dirtyMask = dirtyMask or (1 shl index)
    }

    // --------------------------------------------------------------------------
    // Utility functions
    // --------------------------------------------------------------------------

    fun shouldNotRun(): Nothing {
        throw IllegalStateException("this code should not be reached, please open a bug report, fragment: $this")
    }

    fun invalidIndex(index: Int): Nothing {
        throw IllegalStateException("invalid index: $index")
    }

    fun trace(point: String) {
        adapter.trace(this, point, "")
    }

    fun trace(point: String, bridge: AdaptiveBridge<BT>) {
        adapter.trace(this, point, "bridge: $bridge")
    }

    @OptIn(ExperimentalStdlibApi::class)
    open fun traceWithState(point: String) {
        val thisMask = getThisClosureDirtyMask().toHexString()
        val createMask = getCreateClosureDirtyMask().toHexString()
        adapter.trace(this, point, "createMask: 0x$createMask thisMask: 0x$thisMask state: ${stateToTraceString()}")
    }

    open fun stateToTraceString() : String =
        this.state.contentToString()

    fun traceSupport(point: String, supportFunction: AdaptiveSupportFunction<BT>, arguments: Array<out Any?>) {
        adapter.trace(this, point, "index: ${supportFunction.supportFunctionIndex} arguments: ${arguments.contentToString()}")
    }

    fun traceSupport(point: String, supportFunction: AdaptiveSupportFunction<BT>, result: Any?) {
        adapter.trace(this, point, "index: ${supportFunction.supportFunctionIndex} result: $result")
    }

    override fun toString(): String =
        "${this::class.simpleName ?: "<unknown>"} @ $id"

}