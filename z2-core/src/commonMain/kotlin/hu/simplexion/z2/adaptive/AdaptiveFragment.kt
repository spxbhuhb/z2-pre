/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

interface AdaptiveFragment<BT> {

    val adapter: AdaptiveAdapter<BT>

    val parent: AdaptiveFragment<BT>?

    val closure: AdaptiveClosure<BT>?

    val state: Array<Any?>

    val index : Int

    // functions that support the descendants of this fragment

    fun adaptiveBuild(closure : AdaptiveClosure<BT>, parent : AdaptiveFragment<BT>, index : Int) : AdaptiveFragment<BT>
    fun adaptivePatch(fragment : AdaptiveFragment<BT>, index : Int)
    fun adaptiveSelect(fragment : AdaptiveFragment<BT>, index: Int) : Int

    // functions that operate on the fragment itself

    fun adaptiveCreate()
    fun adaptiveMount(bridge: AdaptiveBridge<BT>)

    fun adaptiveExternalPatch()
    fun adaptiveInternalPatch()

    fun adaptiveUnmount(bridge: AdaptiveBridge<BT>)
    fun adaptiveDispose()

}