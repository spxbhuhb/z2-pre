/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

open class AdaptiveWhen<BT>(
    override val adaptiveAdapter: AdaptiveAdapter<BT>,
    override val adaptiveClosure: AdaptiveClosure<BT>?,
    override val adaptiveParent: AdaptiveFragment<BT>,
    val adaptiveSelect: () -> Int,
    vararg val factories: () -> AdaptiveFragment<BT>
) : AdaptiveFragment<BT> {

    override val adaptiveExternalPatch: AdaptiveExternalPatchType<BT> = {  }

    lateinit var placeholder: AdaptiveBridge<BT>

    var branch = adaptiveSelect()
    var fragment: AdaptiveFragment<BT>? = if (branch == - 1) null else factories[branch]()

    override fun adaptiveCreate() {
        fragment?.adaptiveCreate()
    }

    override fun adaptiveMount(bridge: AdaptiveBridge<BT>) {
        placeholder = adaptiveAdapter.createPlaceholder()
        bridge.add(placeholder)

        fragment?.adaptiveMount(placeholder)
    }

    override fun adaptivePatch() {
        val newBranch = adaptiveSelect()
        if (newBranch == branch) {
            fragment?.let {
                it.adaptiveExternalPatch(it)
                it.adaptivePatch()
            }
        } else {
            fragment?.adaptiveUnmount(placeholder)
            fragment?.adaptiveDispose()
            branch = newBranch
            fragment = if (branch == - 1) null else factories[branch]()
            fragment?.adaptiveCreate()
            fragment?.adaptiveMount(placeholder)
        }
    }

    override fun adaptiveUnmount(bridge: AdaptiveBridge<BT>) {
        fragment?.adaptiveUnmount(placeholder)
        bridge.remove(placeholder)
    }

    override fun adaptiveDispose() {
        fragment?.adaptiveDispose()
    }

}