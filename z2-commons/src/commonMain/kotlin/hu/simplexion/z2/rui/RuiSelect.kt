/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui

open class RuiSelect<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiClosure: RuiClosure<BT>?,
    override val ruiParent: RuiFragment<BT>,
    val ruiSelect: () -> Int,
    vararg val factories: () -> RuiFragment<BT>
) : RuiFragment<BT> {

    override val ruiExternalPatch: RuiExternalPathType<BT> = {  }

    lateinit var placeholder: RuiBridge<BT>

    var branch = ruiSelect()
    var fragment: RuiFragment<BT>? = if (branch == - 1) null else factories[branch]()

    override fun ruiCreate() {
        fragment?.ruiCreate()
    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        placeholder = ruiAdapter.createPlaceholder()
        bridge.add(placeholder)

        fragment?.ruiMount(placeholder)
    }

    override fun ruiPatch() {
        val newBranch = ruiSelect()
        if (newBranch == branch) {
            fragment?.let {
                it.ruiExternalPatch(it)
                it.ruiPatch()
            }
        } else {
            fragment?.ruiUnmount(placeholder)
            fragment?.ruiDispose()
            branch = newBranch
            fragment = if (branch == - 1) null else factories[branch]()
            fragment?.ruiCreate()
            fragment?.ruiMount(placeholder)
        }
    }

    override fun ruiUnmount(bridge: RuiBridge<BT>) {
        fragment?.ruiUnmount(placeholder)
        bridge.remove(placeholder)
    }

    override fun ruiDispose() {
        fragment?.ruiDispose()
    }

}