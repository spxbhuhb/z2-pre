/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.worker.AdaptiveWorker
import hu.simplexion.z2.meta.ValueBinding

abstract class AdaptiveFragment<BT>(
    val adapter: AdaptiveAdapter<BT>,
    val parent: AdaptiveFragment<BT>?,
    val index: Int,
    stateSize: Int
) {

    val id: Long = adapter.newId()

    var trace: Boolean = adapter.trace

    val state: Array<Any?> = arrayOfNulls<Any?>(stateSize)

    @Suppress("LeakingThis") // closure won't do anything with components during init
    open val thisClosure: AdaptiveClosure<BT> = AdaptiveClosure(arrayOf(this), stateSize)

    open val createClosure: AdaptiveClosure<BT>
        get() = parent?.thisClosure ?: thisClosure

    var dirtyMask: AdaptiveStateVariableMask = adaptiveInitStateMask

    var containedFragment: AdaptiveFragment<BT>? = null

    var workers: MutableList<AdaptiveWorker>? = null

    var bindings : MutableList<ValueBinding<*>>? = null

    // --------------------------------------------------------------------------
    // Functions that support the descendants of this fragment
    // --------------------------------------------------------------------------

    open fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? {
        pluginGenerated("genBuild")
    }

    open fun genPatchDescendant(fragment: AdaptiveFragment<BT>) {
        pluginGenerated("genPatchDescendant")
    }

    open fun invoke(supportFunction: AdaptiveSupportFunction, arguments: Array<out Any?>): Any? {
        if (trace) traceSupport("before-Invoke", supportFunction, arguments)

        val result = genInvoke(supportFunction, arguments)

        if (trace) traceSupport("after-Invoke", supportFunction, result)

        return result
    }

    open fun genInvoke(supportFunction: AdaptiveSupportFunction, arguments: Array<out Any?>): Any? {
        pluginGenerated("genInvoke")
    }

    open suspend fun invokeSuspend(supportFunction: AdaptiveSupportFunction, arguments: Array<out Any?>): Any? {
        if (trace) traceSupport("before-Invoke-Suspend", supportFunction, arguments)

        val result = genInvokeSuspend(supportFunction, arguments)

        if (trace) traceSupport("after-Invoke-Suspend", supportFunction, result)

        return result
    }

    open suspend fun genInvokeSuspend(supportFunction: AdaptiveSupportFunction, arguments: Array<out Any?>): Any? {
        pluginGenerated("genInvokeSuspend")
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
        if (trace) trace("before-Mount", "bridge", bridge)

        innerMount(bridge)

        if (trace) trace("after-Mount", "bridge", bridge)
    }

    open fun innerMount(bridge: AdaptiveBridge<BT>) {
        containedFragment?.mount(bridge)
    }

    open fun patch() {
        patchExternal()
        patchInternal()
    }

    open fun patchExternal() {
        if (trace) traceWithState("before-Patch-External")

        if (parent != null) { // root components has no parent which can patch them
            createClosure.owner.genPatchDescendant(this)
        }

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
        pluginGenerated("genPatchInternal")
    }

    open fun unmount(bridge: AdaptiveBridge<BT>) {
        if (trace) trace("before-Unmount", "bridge", bridge)

        innerUnmount(bridge)

        // converting to array so we can safely remove
        workers?.toTypedArray()?.forEach { removeWorker(it) }

        if (trace) trace("after-Unmount", "bridge", bridge)
    }

    open fun innerUnmount(bridge: AdaptiveBridge<BT>) {
        containedFragment?.unmount(bridge)
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
        createClosure.closureDirtyMask()

    fun getCreateClosureVariable(variableIndex: Int): Any? =
        createClosure.get(variableIndex)

    fun getThisClosureVariable(variableIndex: Int): Any? =
        thisClosure.get(variableIndex)

    fun setStateVariable(index: Int, value: Any?) {
        setStateVariable(index, value, null)
    }

    fun setStateVariable(index: Int, value: Any?, origin: ValueBinding<*>?) {
        if (state[index] == value) return

        state[index] = value
        dirtyMask = dirtyMask or (1 shl index)

        bindings?.forEach { if (it !== origin) it.callback?.invoke(it) }

        if (origin != null) {
            patchInternal()
        }
    }

    // --------------------------------------------------------------------------
    // Worker management
    // --------------------------------------------------------------------------

    fun addWorker(worker: AdaptiveWorker) {
        if (trace) trace("before-Add-Worker", "worker", worker)

        val workers = workers ?: mutableListOf<AdaptiveWorker>().also { workers = it }

        workers.filter { worker.replaces(it) }.forEach { other ->
            removeWorker(other)
        }

        workers += worker
        worker.start()

        if (trace) trace("after-Add-Worker", "worker", worker)
    }

    fun removeWorker(worker: AdaptiveWorker) {
        if (trace) trace("before-Remove-Worker", "worker", worker)

        requireNotNull(workers).remove(worker)
        worker.stop()

        if (trace) trace("after-Remove-Worker", "worker", worker)
    }

    // --------------------------------------------------------------------------
    // Binding management
    // --------------------------------------------------------------------------

    fun addValueBinding(binding: ValueBinding<*>) {
        if (trace) trace("before-Add-Binding", "binding", binding)

        val bindings = bindings ?: mutableListOf<ValueBinding<*>>().also { bindings = it }

        bindings.filter { binding.replaces(it) }.forEach { other ->
            removeBinding(other)
        }

        bindings += binding

        if (trace) trace("after-Add-Binding", "binding", binding)
    }

    fun removeBinding(binding: ValueBinding<*>) {
        if (trace) trace("before-Remove-Binding", "binding", binding)

        requireNotNull(bindings).remove(binding)

        if (trace) trace("after-Remove-Binding", "binding", binding)
    }

    // --------------------------------------------------------------------------
    // Utility functions
    // --------------------------------------------------------------------------

    fun pluginGenerated(point: String): Nothing {
        throw IllegalStateException("this code should be replaced by the compiler plugin, please open a bug report, fragment: $this, point: $point")
    }

    fun invalidIndex(index: Int): Nothing {
        throw IllegalStateException("invalid index: $index")
    }

    fun trace(point: String) {
        adapter.trace(this, point, "")
    }

    fun trace(point: String, label: String, value: Any?) {
        adapter.trace(this, point, "$label: $value")
    }

    @OptIn(ExperimentalStdlibApi::class)
    open fun traceWithState(point: String) {
        val thisMask = getThisClosureDirtyMask().toHexString()
        val createMask = getCreateClosureDirtyMask().toHexString()
        adapter.trace(this, point, "createMask: 0x$createMask thisMask: 0x$thisMask state: ${stateToTraceString()}")
    }

    open fun stateToTraceString(): String =
        this.state.contentToString()

    fun traceSupport(point: String, supportFunction: AdaptiveSupportFunction, arguments: Array<out Any?>) {
        adapter.trace(this, point, "$supportFunction arguments: ${arguments.contentToString()}")
    }

    fun traceSupport(point: String, supportFunction: AdaptiveSupportFunction, result: Any?) {
        adapter.trace(this, point, "index: ${supportFunction.supportFunctionIndex} result: $result")
    }

    override fun toString(): String =
        "${this::class.simpleName ?: "<unknown>"} @ $id"

}