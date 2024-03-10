/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.*

abstract class AdaptiveTracingFragment<BT>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveClosure: AdaptiveClosure<BT>?,
    override val adaptiveParent: AdaptiveFragment<BT>?,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT>
) : AdaptiveFragment<BT> {

    val traceName = this::class.simpleName.toString()

    override fun adaptiveCreate() {
        adaptiveAdapter.trace(traceName, "create")
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        adaptiveAdapter.trace(traceName, "mount", "bridge:", bridge)
    }

    override fun adaptivePatch() {
        adaptiveAdapter.trace(traceName, "patch")
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        adaptiveAdapter.trace(traceName, "unmount", "bridge:", bridge)
    }

    override fun adaptiveDispose() {
        adaptiveAdapter.trace(traceName, "dispose")
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
    adaptiveExternalPatch: AdaptiveExternalPatchType<BT>
) : AdaptiveTracingFragment<BT>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch
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
    var p0: Int
) : AdaptiveTracingFragment<BT>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch
) {

    val stateMask_p0 : Int
        get() = 1

    var adaptiveDirtyMask = AdaptiveStateVariableMask(1)

    fun adaptiveInvalidate0(stateVariableIndex: Int) {
        adaptiveAdapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "adaptiveDirty0:", adaptiveDirtyMask)
        adaptiveDirtyMask.invalidate(stateVariableIndex)
    }

    init {
        adaptiveAdapter.trace(traceName, "init")
    }

    override fun adaptiveCreate() {
        adaptiveAdapter.trace(traceName, "create", "p0:", p0)
    }

    override fun adaptivePatch() {
        adaptiveAdapter.trace(traceName, "patch", "adaptiveDirty0:", adaptiveDirtyMask, "p0:", p0)
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
fun H2(i1 : Int, @Adaptive builder: (i2 : Int) -> Unit) {
    builder(i1 + 2)
}

@Suppress("unused")
class AdaptiveH2(
    adaptiveAdapter: AdaptiveAdapter<TestNode>,
    adaptiveClosure: AdaptiveClosure<TestNode>?,
    adaptiveParent: AdaptiveFragment<TestNode>?,
    adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    val i1 : Int,
    @Adaptive builder: (parentScope: AdaptiveFragment<TestNode>) -> AdaptiveFragment<TestNode>
) : AdaptiveC1(adaptiveAdapter, adaptiveClosure, adaptiveParent, adaptiveExternalPatch) {

    override val fragment0 = adaptiveBuilder111(this)

    fun adaptiveBuilder111(parent: AdaptiveFragment<TestNode>?) : AdaptiveFragment<TestNode> {
        return AdaptiveAnonymous(
            adaptiveAdapter,
            AdaptiveClosure(this, emptyArray(), 1),
            adaptiveParent!!,
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

    override fun adaptivePatch() {
        super.adaptivePatch()
        fragment0.adaptivePatch()
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
    var p0: Int,
    var eventHandler: (np0: Int) -> Unit,
) : AdaptiveTracingFragment<TestNode>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch
) {

    init {
        adaptiveAdapter.trace(traceName, "init", "p0:", p0)
        if (adaptiveAdapter is AdaptiveTestAdapter) {
            adaptiveAdapter.fragments += this
        }
    }

    val adaptiveDirtyMask = AdaptiveStateVariableMask(2)

    override fun adaptiveCreate() {
        adaptiveAdapter.trace(traceName, "create")
    }

    @Suppress("unused")
    fun adaptiveInvalidate0(stateVariableIndex: Int) {
        adaptiveAdapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "adaptiveDirty0:", adaptiveDirtyMask)
        adaptiveDirtyMask.invalidate(stateVariableIndex)
    }

    override fun adaptivePatch() {
        adaptiveAdapter.trace(traceName, "patch", "adaptiveDirty0:", adaptiveDirtyMask, "p0:", p0)
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
    var p0: Int,
    var eventHandler: (np0: Int) -> Unit,
) : AdaptiveTracingFragment<TestNode>(
    adaptiveAdapter,
    adaptiveClosure,
    adaptiveParent,
    adaptiveExternalPatch
) {
    init {
        adaptiveAdapter.trace(traceName, "init", "p0:", p0)
        if (adaptiveAdapter is AdaptiveTestAdapter) {
            adaptiveAdapter.fragments += this
        }
    }

    val adaptiveDirtyMask = AdaptiveStateVariableMask(2)

    override fun adaptiveCreate() {
        adaptiveAdapter.trace(traceName, "create")
    }

    @Suppress("unused")
    fun adaptiveInvalidate0(stateVariableIndex: Int) {
        adaptiveAdapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "adaptiveDirty0:", adaptiveDirtyMask)
        adaptiveDirtyMask.invalidate(stateVariableIndex)
    }

    override fun adaptivePatch() {
        adaptiveAdapter.trace(traceName, "patch", "adaptiveDirty0:", adaptiveDirtyMask, "p0:", p0)
        adaptiveDirtyMask.clear()
    }

}