/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui

open class RuiSequence<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiClosure: RuiClosure<BT>?,
    override val ruiParent: RuiFragment<BT>,
    vararg val builders: (parent: RuiFragment<BT>) -> RuiFragment<BT>
) : RuiFragment<BT> {

    override val ruiExternalPatch: RuiExternalPathType<BT> = {  }

    val fragments = builders.map { it.invoke(ruiParent) }

    override fun ruiCreate() {
        for (i in fragments.indices) {
            fragments[i].ruiCreate()
        }
    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        for (i in fragments.indices) {
            fragments[i].ruiMount(bridge)
        }
    }

    override fun ruiPatch() {
        for (fragment in fragments) {
            fragment.ruiExternalPatch(fragment)
            fragment.ruiPatch()
        }
    }

    override fun ruiUnmount(bridge: RuiBridge<BT>) {
        for (i in fragments.indices) {
            fragments[i].ruiUnmount(bridge)
        }
    }

    override fun ruiDispose() {
        for (i in fragments.indices) {
            fragments[i].ruiDispose()
        }
    }

}