/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

/**
 * Interface to implement by fragments generated by the compiler plugin.
 * This interface may define functions the generated fragments use, so
 * we do not have to code them in IR.
 */
abstract class AdaptiveGeneratedFragment<BT>(
    final override val adapter: AdaptiveAdapter<BT>,
    override val parent : AdaptiveFragment<BT>?,
    override val index: Int,
    stateSize : Int
) : AdaptiveFragment<BT> {

    override val id = adapter.newId()

    override val state = arrayOfNulls<Any?>(stateSize)

    override var dirtyMask = adaptiveInitStateMask

    @Suppress("LeakingThis") // closure won't do anything with components during init
    override val thisClosure = AdaptiveClosure(arrayOf(this), stateSize)

    /**
     * The top level fragment of this scope. This may be:
     *
     * - a structural fragment such as block, when or a loop
     * - direct call to another adaptive function
     *
     * Set to [AdaptiveFragment.createClosure] by the initializer of the class
     * that implements [AdaptiveGeneratedFragment].
     */
    lateinit var containedFragment: AdaptiveFragment<BT>

    override fun invoke(supportFunction: AdaptiveSupportFunction<BT>, arguments: Array<out Any?>): Any? {
        shouldNotRun() // this is overridden by the plugin
    }

    override fun create() {
        if (adapter.trace) adapter.trace(this::class.simpleName ?: "<generated>", id, "create")

        createClosure?.owner?.patchExternal(this)

        containedFragment = build(this, 0)

        dirtyMask = adaptiveCleanStateMask
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace(this::class.simpleName ?: "<generated>", id, "mount", "bridge", bridge)
        containedFragment.mount(bridge)
    }

    override fun patchInternal() {
        if (adapter.trace) adapter.trace(this::class.simpleName ?: "<generated>", id, "patch")
        containedFragment.patchInternal()
        dirtyMask = adaptiveCleanStateMask
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) adapter.trace(this::class.simpleName ?: "<generated>", id, "unmount", "bridge", bridge)
        containedFragment.unmount(bridge)
    }

    override fun dispose() {
        if (adapter.trace) adapter.trace(this::class.simpleName ?: "<generated>", id, "dispose")
        containedFragment.dispose()
    }

}