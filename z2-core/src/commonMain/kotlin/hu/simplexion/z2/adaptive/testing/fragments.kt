/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.*

abstract class AdaptiveTracingFragment<BT>: AdaptiveFragment<BT> {

    val traceName = this::class.simpleName.toString()

    override var dirtyMask = adaptiveInitStateMask

    override val createClosure : AdaptiveClosure<BT>?
        get() = parent?.thisClosure

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        shouldNotRun()
    }

    override fun patchExternal(fragment: AdaptiveFragment<BT>) {
        adapter.trace(traceName, id, "patch(fragment)")
    }

    override fun invoke(supportFunction: AdaptiveSupportFunction<BT>, vararg arguments: Any?) {
        adapter.trace(traceName, id, "invoke")
    }

    override fun create() {
        adapter.trace(traceName, id, "create")
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        adapter.trace(traceName, id, "mount", "bridge:", bridge)
    }

    override fun patchInternal() {
        adapter.trace(traceName, id, "patch")
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        adapter.trace(traceName, id, "unmount", "bridge:", bridge)
    }

    override fun dispose() {
        adapter.trace(traceName, id, "dispose")
    }

}

@Suppress("unused", "FunctionName", "UnusedReceiverParameter")
fun Adaptive.T0() {
}

@Suppress("unused")
class AdaptiveT0<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val index: Int
) : AdaptiveTracingFragment<BT>() {

    override val id = adapter.newId()

    override val state = emptyArray<Any?>()

    override val thisClosure = AdaptiveClosure(arrayOf(this), state.size)

}


@Suppress("unused", "FunctionName", "UNUSED_PARAMETER", "UnusedReceiverParameter")
fun Adaptive.T1(p0: Int) {

}

@Suppress("unused")
class AdaptiveT1<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val index: Int
) : AdaptiveTracingFragment<BT>() {

    override val id = adapter.newId()

    override val state = arrayOfNulls<Any?>(1)

    override val thisClosure = AdaptiveClosure(arrayOf(this), state.size)

    var p0 : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    val stateMask_p0: Int
        get() = 1

    override fun create() {
        createClosure?.owner?.patchExternal(this)
        adapter.trace(traceName, id, "create", "p0:", p0)
        dirtyMask = adaptiveCleanStateMask
    }

    override fun patchInternal() {
        createClosure?.owner?.patchExternal(this)
        adapter.trace(traceName, id, "patch", "dirtyMask:", dirtyMask, "p0:", p0)
    }
}

@Suppress("unused")
fun Adaptive.H1(builder: Adaptive.() -> Unit) {
    builder()
}

@Suppress("unused")
class AdaptiveH1<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val index: Int
) : AdaptiveTracingFragment<BT>() {

    override val id = adapter.newId()

    override val state = arrayOfNulls<Any?>(1)

    override val thisClosure = AdaptiveClosure(arrayOf(this), state.size)

    @Suppress("UNCHECKED_CAST")
    var builder : AdaptiveFragmentFactory<BT>
        get() = state[0] as AdaptiveFragmentFactory<BT>
        set(v) { state[0] = v }

    lateinit var fragment0: AdaptiveFragment<BT>

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {

        val fragment = when (declarationIndex) {
            0 -> builder.build(parent)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patchExternal(fragment: AdaptiveFragment<BT>) {
        super.patchExternal(fragment)
        fragment0.patchInternal()
    }

    override fun create() {
        super.create()
        createClosure!!.owner.patchExternal(this)

        fragment0 = build(this, 0) // 0 is the index of the `builder` call
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        super.mount(bridge)
        fragment0.mount(bridge)
    }

    override fun patchInternal() {
        super.patchInternal()
        fragment0.patchInternal()
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        fragment0.unmount(bridge)
        super.unmount(bridge)
    }

    override fun dispose() {
        fragment0.dispose()
        super.dispose()
    }
}

@Suppress("unused")
fun Adaptive.H2(i1: Int, builder: Adaptive.(i2: Int) -> Unit) {
    builder(i1 + 2)
}

@Suppress("unused")
class AdaptiveH2<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val index: Int
) : AdaptiveTracingFragment<BT>() {

    override val id = adapter.newId()

    override val state = arrayOfNulls<Any?>(2)

    override val thisClosure = AdaptiveClosure(arrayOf(this), state.size)

    @Suppress("UNCHECKED_CAST")
    var builder : AdaptiveFragmentFactory<BT>
        get() = state[0] as AdaptiveFragmentFactory<BT>
        set(v) { state[0] = v }

    var i1 : Int
        get() = state[1] as Int
        set(v) { state[1] = v }

    lateinit var fragment0: AdaptiveFragment<BT>

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {

        val fragment = when (declarationIndex) {
            0 -> builder.build(this)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patchExternal(fragment: AdaptiveFragment<BT>) {
        super.patchExternal(fragment)

        when (fragment.index) {
            0 -> {
                fragment0.state[0] = i1 + 2
            }
            else -> invalidIndex(fragment.index)
        }
    }

    override fun create() {
        super.create()

        createClosure!!.owner.patchExternal(this)

        fragment0 = build(this, 0) // 0 is the index of the `builder` call
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        super.mount(bridge)
        fragment0.mount(bridge)
    }

    override fun patchInternal() {
        super.patchInternal()
        fragment0.patchInternal()
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        fragment0.unmount(bridge)
        super.unmount(bridge)
    }

    override fun dispose() {
        fragment0.dispose()
        super.dispose()
    }

}


@Suppress("unused", "FunctionName", "UNUSED_PARAMETER", "UnusedReceiverParameter")
fun Adaptive.EH1A(p0: Int, eventHandler: (np0: Int) -> Unit) {
}

@Suppress("unused")
class AdaptiveEH1A<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    override val parent: AdaptiveFragment<BT>?,
    override val index: Int
) : AdaptiveTracingFragment<BT>() {

    override val id = adapter.newId()

    override val state = arrayOfNulls<Any?>(2)

    override val thisClosure = AdaptiveClosure(arrayOf(this), state.size)

    var p0 : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    @Suppress("UNCHECKED_CAST")
    var eventHandler : AdaptiveSupportFunction<BT>
        get() = state[1] as AdaptiveSupportFunction<BT>
        set(v) { state[1] = v }

    override fun patchInternal() {
        adapter.trace(traceName, id, "patch", "dirtyMask:", dirtyMask, "p0:", p0)

        if (p0 % 2 == 0) {
            eventHandler.invoke(p0)
        }

        dirtyMask = 0
    }

}