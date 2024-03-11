/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

open class AdaptiveSequence<BT>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveClosure: AdaptiveClosure<BT>?,
    override val adaptiveParent: AdaptiveFragment<BT>,
) : AdaptiveFragment<BT> {

    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT> = {  }

    val fragments = mutableListOf<AdaptiveFragment<BT>>()

    fun add(fragment : AdaptiveFragment<BT>) {
        fragments += fragment
    }

    override fun adaptiveCreate() {
        for (i in fragments.indices) {
            fragments[i].adaptiveCreate()
        }
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        for (i in fragments.indices) {
            fragments[i].adaptiveMount(bridge)
        }
    }

    override fun adaptivePatch() {
        for (fragment in fragments) {
            fragment.adaptiveExternalPatch(fragment)
            fragment.adaptivePatch()
        }
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        for (i in fragments.indices) {
            fragments[i].adaptiveUnmount(bridge)
        }
    }

    override fun adaptiveDispose() {
        for (i in fragments.indices) {
            fragments[i].adaptiveDispose()
        }
    }

}