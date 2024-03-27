/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.*

abstract class AdaptiveTracingFragment<BT>: AdaptiveFragment<BT> {

    val traceName = this::class.simpleName.toString()

    override var dirtyMask = 0

    override val createClosure : AdaptiveClosure<BT>?
        get() = parent?.thisClosure

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        shouldNotRun()
    }

    override fun patch(fragment: AdaptiveFragment<BT>) {
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

    override fun patch() {
        adapter.trace(traceName, id, "patch")
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        adapter.trace(traceName, id, "unmount", "bridge:", bridge)
    }

    override fun dispose() {
        adapter.trace(traceName, id, "dispose")
    }

}

@Adaptive
@Suppress("unused", "FunctionName")
fun T0() {
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

@Adaptive
@Suppress("unused", "FunctionName", "UNUSED_PARAMETER")
fun T1(p0: Int) {
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

    var adaptiveDirtyMask = AdaptiveStateVariableMask(1)

    fun adaptiveInvalidate0(stateVariableIndex: Int) {
        adapter.trace(traceName, id, "invalidate", "stateVariableIndex:", stateVariableIndex, "adaptiveDirty0:", adaptiveDirtyMask)
        adaptiveDirtyMask.invalidate(stateVariableIndex)
    }

    override fun create() {
        createClosure?.owner?.patch(this)
        adapter.trace(traceName, id, "create", "p0:", p0)
    }

    override fun patch() {
        createClosure?.owner?.patch(this)
        adapter.trace(traceName, id, "patch", "dirtyMask:", dirtyMask, "p0:", p0)
    }
}

@Suppress("unused")
@Adaptive
fun H1(@Adaptive builder: () -> Unit) {
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

    override fun patch(fragment: AdaptiveFragment<BT>) {
        super.patch(fragment)
        fragment0.patch()
    }

    override fun create() {
        super.create()
        createClosure!!.owner.patch(this)

        fragment0 = build(this, 0) // 0 is the index of the `builder` call
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        super.mount(bridge)
        fragment0.mount(bridge)
    }

    override fun patch() {
        super.patch()
        fragment0.patch()
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
@Adaptive
fun H2(i1: Int, @Adaptive builder: (i2: Int) -> Unit) {
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

    override fun patch(fragment: AdaptiveFragment<BT>) {
        super.patch(fragment)

        when (fragment.index) {
            0 -> {
                fragment0.state[0] = i1 + 2
            }
            else -> invalidIndex(fragment.index)
        }
    }

    override fun create() {
        super.create()

        createClosure!!.owner.patch(this)

        fragment0 = build(this, 0) // 0 is the index of the `builder` call
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        super.mount(bridge)
        fragment0.mount(bridge)
    }

    override fun patch() {
        super.patch()
        fragment0.patch()
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


@Adaptive
@Suppress("unused", "FunctionName", "UNUSED_PARAMETER")
fun EH1A(p0: Int, eventHandler: (np0: Int) -> Unit) {
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

    val adaptiveDirtyMask = AdaptiveStateVariableMask(2)

    override fun patch() {
        adapter.trace(traceName, id, "patch", "adaptiveDirty0:", adaptiveDirtyMask, "p0:", p0)

        if (p0 % 2 == 0) {
            eventHandler.invoke(p0)
        }

        adaptiveDirtyMask.clear()
    }

}