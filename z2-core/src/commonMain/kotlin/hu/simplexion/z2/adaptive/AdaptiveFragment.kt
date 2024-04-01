/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

interface AdaptiveFragment<BT> {

    val id: Long

    val adapter: AdaptiveAdapter<BT>

    val parent: AdaptiveFragment<BT>?

    val createClosure: AdaptiveClosure<BT>?
        get() = parent?.thisClosure

    val thisClosure: AdaptiveClosure<BT>

    val index: Int

    val state: Array<Any?>

    var dirtyMask: AdaptiveStateVariableMask

    // functions that support the descendants of this fragment

    fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>?
    fun patchDescendant(fragment: AdaptiveFragment<BT>)
    fun invoke(supportFunction: AdaptiveSupportFunction<BT>, arguments: Array<out Any?>): Any?

    // functions that operate on the fragment itself

    fun create()
    fun mount(bridge: AdaptiveBridge<BT>)

    fun patch() {
        patchExternal()
        patchInternal()
    }

    fun patchExternal()
    fun patchInternal()

    fun unmount(bridge: AdaptiveBridge<BT>)
    fun dispose()

    // state access functions

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

    // utility functions

    fun invalidIndex(index: Int): Nothing {
        throw IllegalStateException("invalid index: $index")
    }

    fun trace(point : String) {
        adapter.trace(this, point, "")
    }

    fun trace(point : String, bridge: AdaptiveBridge<BT>) {
        adapter.trace(this, point, "bridge: $bridge")
    }

    fun traceWithState(point : String) {
        adapter.trace(this, point, "closureDirtyMask: ${getCreateClosureDirtyMask()} state: ${this.state.contentToString()}")
    }

    fun trace(supportFunction: AdaptiveSupportFunction<BT>, arguments: Array<out Any?>) {
        adapter.trace(this, "invoke", "index: ${supportFunction.supportFunctionIndex} arguments: ${arguments.contentToString()}")
    }

}