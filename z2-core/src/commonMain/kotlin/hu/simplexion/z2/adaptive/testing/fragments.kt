/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.*

abstract class AdaptiveTracingFragment<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val closure: AdaptiveClosure<BT>?,
    override val parent: AdaptiveFragment<BT>?,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT>,
) : AdaptiveFragment<BT> {

    val traceName = this::class.simpleName.toString()

    override fun adaptiveCreate() {
        adapter.trace(traceName, "create")
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        adapter.trace(traceName, "mount", "bridge:", bridge)
    }

    override fun adaptiveInternalPatch() {
        adapter.trace(traceName, "patch")
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        adapter.trace(traceName, "unmount", "bridge:", bridge)
    }

    override fun adaptiveDispose() {
        adapter.trace(traceName, "dispose")
    }

}

@Adaptive
@Suppress("unused", "FunctionName")
fun T0() {
}

@Suppress("unused")
class AdaptiveT0<BT>(
    adaptiveAdapter: AdaptiveAdapter<BT>,
    adaptiveClosure: AdaptiveClosure<BT>?,
    adaptiveParent: AdaptiveFragment<BT>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<BT>,
    override val state: Array<Any?>
) : AdaptiveTracingFragment<BT>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch,
)

@Adaptive
@Suppress("unused", "FunctionName", "UNUSED_PARAMETER")
fun T1(p0: Int) {
}

@Suppress("unused")
class AdaptiveT1<BT>(
    adaptiveAdapter: AdaptiveAdapter<BT>,
    adaptiveClosure: AdaptiveClosure<BT>?,
    adaptiveParent: AdaptiveFragment<BT>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<BT>,
    override val state: Array<Any?>
) : AdaptiveTracingFragment<BT>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch
) {

    var p0 : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    val stateMask_p0: Int
        get() = 1

    var adaptiveDirtyMask = AdaptiveStateVariableMask(1)

    fun adaptiveInvalidate0(stateVariableIndex: Int) {
        adapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "adaptiveDirty0:", adaptiveDirtyMask)
        adaptiveDirtyMask.invalidate(stateVariableIndex)
    }

    init {
        adaptiveAdapter.trace(traceName, "init")
    }

    override fun adaptiveCreate() {
        adapter.trace(traceName, "create", "p0:", p0)
    }

    override fun adaptiveInternalPatch() {
        adapter.trace(traceName, "patch", "adaptiveDirty0:", adaptiveDirtyMask, "p0:", p0)
        adaptiveDirtyMask.clear()
    }
}

@Suppress("unused")
@Adaptive
fun H1(@Adaptive builder: () -> Unit) {
    builder()
}

@Suppress("unused")
class AdaptiveH1(
    adaptiveAdapter: AdaptiveAdapter<TestNode>,
    adaptiveClosure: AdaptiveClosure<TestNode>?,
    adaptiveParent: AdaptiveFragment<TestNode>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    override val state: Array<Any?>,
    @Adaptive val builder: (adaptiveAdapter: AdaptiveAdapter<TestNode>) -> AdaptiveFragment<TestNode>
) : AdaptiveC1(adaptiveAdapter, adaptiveClosure, adaptiveParent, adaptiveExternalPatch) {

    override val fragment0 = builder(adaptiveAdapter)

    override fun adaptiveMount(bridge: AdaptiveBridge<TestNode>) {
        super.adaptiveMount(bridge)
        fragment0.adaptiveMount(bridge)
    }

    init {
        adaptiveAdapter.trace(traceName, "init")
    }
}

@Suppress("unused")
@Adaptive
fun H2(i1: Int, @Adaptive builder: (i2: Int) -> Unit) {
    builder(i1 + 2)
}

@Suppress("unused")
class AdaptiveH2(
    adaptiveAdapter: AdaptiveAdapter<TestNode>,
    adaptiveClosure: AdaptiveClosure<TestNode>?,
    adaptiveParent: AdaptiveFragment<TestNode>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    override val state: Array<Any?>,
    @Adaptive builder: (parentScope: AdaptiveFragment<TestNode>) -> AdaptiveFragment<TestNode>
) : AdaptiveC1(adaptiveAdapter, adaptiveClosure, adaptiveParent, adaptiveExternalPatch) {

    var i1 : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    override val fragment0 = adaptiveBuilder111(this)

    fun adaptiveBuilder111(parent: AdaptiveFragment<TestNode>?): AdaptiveFragment<TestNode> {
        return AdaptiveAnonymous(
            adapter,
            AdaptiveClosure(this, emptyArray(), 1),
            this.parent !!,
            this::adaptiveExternalPatch111,
            arrayOf(i1 + 1)
        )
    }

    fun adaptiveExternalPatch111(it: AdaptiveFragment<TestNode>) {

    }

    override fun adaptiveMount(bridge: AdaptiveBridge<TestNode>) {
        super.adaptiveMount(bridge)
        fragment0.adaptiveMount(bridge)
    }

    init {
        adaptiveAdapter.trace(traceName, "init")
    }

    // FIXME finish ADAPTIVE H2 test fragment
}

@Suppress("unused")
abstract class AdaptiveC1(
    adaptiveAdapter: AdaptiveAdapter<TestNode>,
    adaptiveClosure: AdaptiveClosure<TestNode>?,
    adaptiveParent: AdaptiveFragment<TestNode>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>
) : AdaptiveTracingFragment<TestNode>(adaptiveAdapter, adaptiveClosure, adaptiveParent, adaptiveExternalPatch) {

    abstract val fragment0: AdaptiveFragment<TestNode>

    override fun adaptiveCreate() {
        super.adaptiveCreate()
        fragment0.adaptiveCreate()
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<TestNode>) {
        super.adaptiveMount(bridge)
        fragment0.adaptiveMount(bridge)
    }

    override fun adaptiveInternalPatch() {
        super.adaptiveInternalPatch()
        fragment0.adaptiveInternalPatch()
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<TestNode>) {
        fragment0.adaptiveUnmount(bridge)
        super.adaptiveUnmount(bridge)
    }

    override fun adaptiveDispose() {
        fragment0.adaptiveDispose()
        super.adaptiveDispose()
    }
}

@Adaptive
@Suppress("unused", "FunctionName", "UNUSED_PARAMETER")
fun EH1A(p0: Int, eventHandler: (np0: Int) -> Unit) {
}

@Suppress("unused")
class AdaptiveEH1A(
    adaptiveAdapter: AdaptiveAdapter<TestNode>,
    adaptiveClosure: AdaptiveClosure<TestNode>?,
    adaptiveParent: AdaptiveFragment<TestNode>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    override val state: Array<Any?>
) : AdaptiveTracingFragment<TestNode>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch
) {

    var p0 : Int
        get() = state[0] as Int
        set(v) { state[0] = v }


    init {
        adaptiveAdapter.trace(traceName, "init", "p0:", p0)
        if (adaptiveAdapter is AdaptiveTestAdapter) {
            adaptiveAdapter.fragments += this
        }
    }

    val adaptiveDirtyMask = AdaptiveStateVariableMask(2)

    override fun adaptiveCreate() {
        adapter.trace(traceName, "create")
    }

    @Suppress("unused")
    fun adaptiveInvalidate0(stateVariableIndex: Int) {
        adapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "adaptiveDirty0:", adaptiveDirtyMask)
        adaptiveDirtyMask.invalidate(stateVariableIndex)
    }

    override fun adaptiveInternalPatch() {
        adapter.trace(traceName, "patch", "adaptiveDirty0:", adaptiveDirtyMask, "p0:", p0)
        adaptiveDirtyMask.clear()
    }

}

@Adaptive
@Suppress("unused", "FunctionName", "UNUSED_PARAMETER")
fun EH1B(p0: Int, eventHandler: (np0: Int) -> Unit) {
}

@Suppress("unused")
class AdaptiveEH1B(
    adaptiveAdapter: AdaptiveAdapter<TestNode>,
    adaptiveClosure: AdaptiveClosure<TestNode>?,
    adaptiveParent: AdaptiveFragment<TestNode>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    override val state: Array<Any?>
) : AdaptiveTracingFragment<TestNode>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch
) {

    var p0 : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    init {
        adaptiveAdapter.trace(traceName, "init", "p0:", p0)
        if (adaptiveAdapter is AdaptiveTestAdapter) {
            adaptiveAdapter.fragments += this
        }
    }

    val adaptiveDirtyMask = AdaptiveStateVariableMask(2)

    override fun adaptiveCreate() {
        adapter.trace(traceName, "create")
    }

    @Suppress("unused")
    fun adaptiveInvalidate0(stateVariableIndex: Int) {
        adapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "adaptiveDirty0:", adaptiveDirtyMask)
        adaptiveDirtyMask.invalidate(stateVariableIndex)
    }

    override fun adaptiveInternalPatch() {
        adapter.trace(traceName, "patch", "adaptiveDirty0:", adaptiveDirtyMask, "p0:", p0)
        adaptiveDirtyMask.clear()
    }

}


@Suppress("unused")
@Adaptive
fun RunOnMount(func: () -> Unit) {

}

@Suppress("unused")
class AdaptiveRunOnMount(
    adaptiveAdapter: AdaptiveAdapter<TestNode>,
    adaptiveClosure: AdaptiveClosure<TestNode>?,
    adaptiveParent: AdaptiveFragment<TestNode>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    override val state: Array<Any?>,
) : AdaptiveTracingFragment<TestNode>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch
) {

    @Suppress("UNCHECKED_CAST")
    var func : () -> Unit
        get() = state[0] as () -> Unit
        set(v) { state[0] = v }

    init {
        adaptiveAdapter.trace(traceName, "init")
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<TestNode>) {
        super.adaptiveMount(bridge)
        func()
    }

}