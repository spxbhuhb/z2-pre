/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui

open class RuiBlock<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiScope : RuiFragment<BT>,
    override val ruiCallSiteDependencyMask: RuiStateVariableMask,
    vararg val builders: (parentScope: RuiFragment<BT>) -> RuiFragment<BT>
) : RuiFragment<BT> {

    override val ruiStateSize: Int
        get() = 0

    override val ruiExternalPatch: RuiExternalPathType<BT> = { _, scopeMask -> scopeMask }

    val fragments = builders.map { it.invoke(ruiScope) }

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

    override fun ruiPatch(dirtyMaskOfScope: RuiStateVariableMask) {
        for (fragment in fragments) {
            val extendedScopeMask = fragment.ruiExternalPatch(fragment, dirtyMaskOfScope)
            if (extendedScopeMask != 0) fragment.ruiPatch(extendedScopeMask)
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