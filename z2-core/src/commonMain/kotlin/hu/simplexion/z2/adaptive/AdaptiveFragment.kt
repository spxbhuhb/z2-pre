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

    var trace : Boolean = adapter.trace

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

    open fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        pluginGenerated()
    }

    open fun genPatchDescendant(fragment: AdaptiveFragment<BT>) {
        pluginGenerated()
    }

    open fun invoke(supportFunction: AdaptiveSupportFunction<BT>, callingFragment: AdaptiveFragment<BT>, arguments: Array<out Any?>): Any? {
        if (trace) traceSupport("before-Invoke", supportFunction, callingFragment, arguments)

        val result = genInvoke(supportFunction, callingFragment, arguments)

        if (thisClosure.closureDirtyMask() != adaptiveCleanStateMask) {
            patchInternal()
        }

        if (trace) traceSupport("after-Invoke", supportFunction, result)

        return result
    }

    open fun genInvoke(supportFunction: AdaptiveSupportFunction<BT>, callingFragment: AdaptiveFragment<BT>, arguments: Array<out Any?>): Any? {
        pluginGenerated()
    }

    // --------------------------------------------------------------------------
    // Functions that operate on the fragment itself
    // --------------------------------------------------------------------------

    open fun create() {
        if (trace) trace("before-Create")

        patch()

        containedFragment = genBuild(this, 0)

        if (trace) trace("after-Create")
    }

    open fun mount(bridge: AdaptiveBridge<BT>) {
        if (trace) trace("before-Mount", bridge)

        containedFragment?.mount(bridge)

        if (trace) trace("after-Mount", bridge)
    }

    open fun patch() {
        patchExternal()
        patchInternal()
    }

    open fun patchExternal() {
        if (trace) traceWithState("before-Patch-External")

        createClosure?.owner?.genPatchDescendant(this)

        if (trace) traceWithState("after-Patch-External")
    }

    open fun patchInternal() {
        if (trace) traceWithState("before-Patch-Internal")

        genPatchInternal()

        containedFragment?.patch()

        dirtyMask = adaptiveCleanStateMask

        if (trace) traceWithState("after-Patch-Internal")
    }

    open fun genPatchInternal() {
        pluginGenerated()
    }

    open fun unmount(bridge: AdaptiveBridge<BT>) {
        if (trace) trace("before-Unmount", bridge)

        containedFragment?.unmount(bridge)

        if (trace) trace("after-Unmount", bridge)
    }

    open fun dispose() {
        if (trace) trace("before-Dispose")

        containedFragment?.dispose()

        if (trace) trace("after-Dispose")
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

    fun pluginGenerated(): Nothing {
        throw IllegalStateException("this code should be replaced by the compiler plugin, please open a bug report, fragment: $this")
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

    fun traceSupport(point: String, supportFunction: AdaptiveSupportFunction<BT>, callingFragment: AdaptiveFragment<BT>, arguments: Array<out Any?>) {
        adapter.trace(this, point, "callingFragment: $callingFragment index: ${supportFunction.supportFunctionIndex} arguments: ${arguments.contentToString()}")
    }

    fun traceSupport(point: String, supportFunction: AdaptiveSupportFunction<BT>, result: Any?) {
        adapter.trace(this, point, "index: ${supportFunction.supportFunctionIndex} result: $result")
    }

    override fun toString(): String =
        "${this::class.simpleName ?: "<unknown>"} @ $id"

}