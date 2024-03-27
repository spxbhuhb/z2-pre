/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

interface AdaptiveFragment<BT> {

    val id : Long

    val adapter: AdaptiveAdapter<BT>

    val parent: AdaptiveFragment<BT>?

    val createClosure: AdaptiveClosure<BT>?
        get() = parent?.thisClosure

    val thisClosure: AdaptiveClosure<BT>

    val index : Int

    val state: Array<Any?>

    var dirtyMask : Int

    // functions that support the descendants of this fragment

    fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int) : AdaptiveFragment<BT>
    fun patch(fragment : AdaptiveFragment<BT>)
    fun invoke(supportFunction: AdaptiveSupportFunction<BT>, arguments : Array<out Any?>) : Any?

    // functions that operate on the fragment itself

    fun create()
    fun mount(bridge: AdaptiveBridge<BT>)

    fun patch()

    fun unmount(bridge: AdaptiveBridge<BT>)
    fun dispose()

    // state access functions

    fun getClosureVariableFromLast(variableIndex : Int) : Any? =
        checkNotNull(createClosure).getFromLast(variableIndex)

    fun getClosureVariable(variableIndex : Int) : Any? =
        checkNotNull(createClosure).get(variableIndex)

    fun set(index : Int, value : Any?) {
        state[index] = value
        dirtyMask = dirtyMask or (1 shl index)
    }

    // utility functions

    fun invalidIndex(index: Int) : Nothing {
        throw IllegalStateException("invalid index: $index")
    }
}