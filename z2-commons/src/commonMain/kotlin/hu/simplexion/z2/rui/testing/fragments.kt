/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.rui.testing

import hu.simplexion.z2.rui.*

abstract class RuiTracingFragment<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiClosure: RuiClosure<BT>?,
    override val ruiParent: RuiFragment<BT>?,
    override val ruiExternalPatch: RuiExternalPathType<BT>
) : RuiFragment<BT> {

    val traceName = this::class.simpleName.toString()

    override fun ruiCreate() {
        ruiAdapter.trace(traceName, "create")
    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        ruiAdapter.trace(traceName, "mount", "bridge:", bridge)
    }

    override fun ruiPatch() {
        ruiAdapter.trace(traceName, "patch")
    }

    override fun ruiUnmount(bridge: RuiBridge<BT>) {
        ruiAdapter.trace(traceName, "unmount", "bridge:", bridge)
    }

    override fun ruiDispose() {
        ruiAdapter.trace(traceName, "dispose")
    }

}

@Rui
@Suppress("unused", "FunctionName")
fun T0() {
}

@Suppress("unused")
class RuiT0<BT>(
    ruiAdapter: RuiAdapter<BT>,
    ruiClosure: RuiClosure<BT>?,
    ruiParent: RuiFragment<BT>?,
    ruiExternalPatch: RuiExternalPathType<BT>
) : RuiTracingFragment<BT>(
    ruiAdapter,
    ruiClosure,
    ruiParent,
    ruiExternalPatch
)

@Rui
@Suppress("unused", "FunctionName", "UNUSED_PARAMETER")
fun T1(p0: Int) {
}

@Suppress("unused")
class RuiT1<BT>(
    ruiAdapter: RuiAdapter<BT>,
    ruiClosure: RuiClosure<BT>?,
    ruiParent: RuiFragment<BT>?,
    ruiExternalPatch: RuiExternalPathType<BT>,
    var p0: Int
) : RuiTracingFragment<BT>(
    ruiAdapter,
    ruiClosure,
    ruiParent,
    ruiExternalPatch
) {

    val stateMask_p0 : Int
        get() = 1

    var ruiDirtyMask = RuiStateVariableMask(1)

    fun ruiInvalidate0(stateVariableIndex: Int) {
        ruiAdapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "ruiDirty0:", ruiDirtyMask)
        ruiDirtyMask.invalidate(stateVariableIndex)
    }

    init {
        ruiAdapter.trace(traceName, "init")
    }

    override fun ruiCreate() {
        ruiAdapter.trace(traceName, "create", "p0:", p0)
    }

    override fun ruiPatch() {
        ruiAdapter.trace(traceName, "patch", "ruiDirty0:", ruiDirtyMask, "p0:", p0)
        ruiDirtyMask.clear()
    }
}

@Suppress("unused")
@Rui
fun H1(@Rui builder: () -> Unit) {
    builder()
}

@Suppress("unused")
class RuiH1(
    ruiAdapter: RuiAdapter<TestNode>,
    ruiClosure: RuiClosure<TestNode>?,
    ruiParent: RuiFragment<TestNode>?,
    ruiExternalPatch: RuiExternalPathType<TestNode>,
    @Rui val builder: (ruiAdapter: RuiAdapter<TestNode>) -> RuiFragment<TestNode>
) : RuiC1(ruiAdapter, ruiClosure, ruiParent, ruiExternalPatch) {

    override val fragment0 = builder(ruiAdapter)

    override fun ruiMount(bridge: RuiBridge<TestNode>) {
        super.ruiMount(bridge)
        fragment0.ruiMount(bridge)
    }

    init {
        ruiAdapter.trace(traceName, "init")
    }
}

@Suppress("unused")
@Rui
fun H2(i1 : Int, @Rui builder: (i2 : Int) -> Unit) {
    builder(i1 + 2)
}

@Suppress("unused")
class RuiH2(
    ruiAdapter: RuiAdapter<TestNode>,
    ruiClosure: RuiClosure<TestNode>?,
    ruiParent: RuiFragment<TestNode>?,
    ruiExternalPatch: RuiExternalPathType<TestNode>,
    val i1 : Int,
    @Rui builder: (parentScope: RuiFragment<TestNode>) -> RuiFragment<TestNode>
) : RuiC1(ruiAdapter, ruiClosure, ruiParent, ruiExternalPatch) {

    override val fragment0 = ruiBuilder111(this)

    fun ruiBuilder111(parent: RuiFragment<TestNode>?) : RuiFragment<TestNode> {
        return RuiAnonymous(
            ruiAdapter,
            RuiClosure(this, emptyArray(), 1),
            ruiParent!!,
            this::ruiExternalPatch111,
            arrayOf(i1 + 1)
        )
    }

    fun ruiExternalPatch111(it: RuiFragment<TestNode>) {

    }

    override fun ruiMount(bridge: RuiBridge<TestNode>) {
        super.ruiMount(bridge)
        fragment0.ruiMount(bridge)
    }

    init {
        ruiAdapter.trace(traceName, "init")
    }

    // FIXME finish RUI H2 test fragment
}

@Suppress("unused")
abstract class RuiC1(
    ruiAdapter: RuiAdapter<TestNode>,
    ruiClosure: RuiClosure<TestNode>?,
    ruiParent: RuiFragment<TestNode>?,
    ruiExternalPatch: RuiExternalPathType<TestNode>
) : RuiTracingFragment<TestNode>(ruiAdapter, ruiClosure, ruiParent, ruiExternalPatch) {

    abstract val fragment0: RuiFragment<TestNode>

    override fun ruiCreate() {
        super.ruiCreate()
        fragment0.ruiCreate()
    }

    override fun ruiMount(bridge: RuiBridge<TestNode>) {
        super.ruiMount(bridge)
        fragment0.ruiMount(bridge)
    }

    override fun ruiPatch() {
        super.ruiPatch()
        fragment0.ruiPatch()
    }

    override fun ruiUnmount(bridge: RuiBridge<TestNode>) {
        fragment0.ruiUnmount(bridge)
        super.ruiUnmount(bridge)
    }

    override fun ruiDispose() {
        fragment0.ruiDispose()
        super.ruiDispose()
    }
}

@Rui
@Suppress("unused", "FunctionName", "UNUSED_PARAMETER")
fun EH1A(p0: Int, eventHandler: (np0: Int) -> Unit) {
}

@Suppress("unused")
class RuiEH1A(
    ruiAdapter: RuiAdapter<TestNode>,
    ruiClosure: RuiClosure<TestNode>?,
    ruiParent: RuiFragment<TestNode>?,
    ruiExternalPatch: RuiExternalPathType<TestNode>,
    var p0: Int,
    var eventHandler: (np0: Int) -> Unit,
) : RuiTracingFragment<TestNode>(
    ruiAdapter,
    ruiClosure,
    ruiParent,
    ruiExternalPatch
) {

    init {
        ruiAdapter.trace(traceName, "init", "p0:", p0)
        if (ruiAdapter is RuiTestAdapter) {
            ruiAdapter.fragments += this
        }
    }

    val ruiDirtyMask = RuiStateVariableMask(2)

    override fun ruiCreate() {
        ruiAdapter.trace(traceName, "create")
    }

    @Suppress("unused")
    fun ruiInvalidate0(stateVariableIndex: Int) {
        ruiAdapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "ruiDirty0:", ruiDirtyMask)
        ruiDirtyMask.invalidate(stateVariableIndex)
    }

    override fun ruiPatch() {
        ruiAdapter.trace(traceName, "patch", "ruiDirty0:", ruiDirtyMask, "p0:", p0)
        ruiDirtyMask.clear()
    }

}

@Rui
@Suppress("unused", "FunctionName", "UNUSED_PARAMETER")
fun EH1B(p0: Int, eventHandler: (np0: Int) -> Unit) {
}

@Suppress("unused")
class RuiEH1B(
    ruiAdapter: RuiAdapter<TestNode>,
    ruiClosure: RuiClosure<TestNode>?,
    ruiParent: RuiFragment<TestNode>?,
    ruiExternalPatch: RuiExternalPathType<TestNode>,
    var p0: Int,
    var eventHandler: (np0: Int) -> Unit,
) : RuiTracingFragment<TestNode>(
    ruiAdapter,
    ruiClosure,
    ruiParent,
    ruiExternalPatch
) {
    init {
        ruiAdapter.trace(traceName, "init", "p0:", p0)
        if (ruiAdapter is RuiTestAdapter) {
            ruiAdapter.fragments += this
        }
    }

    val ruiDirtyMask = RuiStateVariableMask(2)

    override fun ruiCreate() {
        ruiAdapter.trace(traceName, "create")
    }

    @Suppress("unused")
    fun ruiInvalidate0(stateVariableIndex: Int) {
        ruiAdapter.trace(traceName, "invalidate", "stateVariableIndex:", stateVariableIndex, "ruiDirty0:", ruiDirtyMask)
        ruiDirtyMask.invalidate(stateVariableIndex)
    }

    override fun ruiPatch() {
        ruiAdapter.trace(traceName, "patch", "ruiDirty0:", ruiDirtyMask, "p0:", p0)
        ruiDirtyMask.clear()
    }

}