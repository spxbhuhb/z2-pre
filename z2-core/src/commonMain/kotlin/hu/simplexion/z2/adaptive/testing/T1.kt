/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptivePlaceholder

@Suppress("unused", "FunctionName", "UNUSED_PARAMETER", "UnusedReceiverParameter")
fun Adaptive.T1(p0: Int) {

}

@Suppress("unused")
class AdaptiveT1<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 1) {

    override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        return AdaptivePlaceholder(adapter, parent, -1)
    }

    override fun patchDescendant(fragment: AdaptiveFragment<BT>) {

    }

    override fun generatedPatchInternal() {

    }

    var p0 : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    val stateMask_p0: Int
        get() = 1

}