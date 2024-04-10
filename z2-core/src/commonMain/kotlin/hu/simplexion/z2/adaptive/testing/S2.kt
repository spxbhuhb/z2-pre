/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("FunctionName")

package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.*

@Suppress("unused", "FunctionName", "UNUSED_PARAMETER", "UnusedReceiverParameter")
fun Adaptive.SuspendS1(supportFun : suspend (i : Int) -> Unit) {

}

@Suppress("unused")
class AdaptiveSuspendS1<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 1) {

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        return AdaptivePlaceholder(adapter, parent, -1)
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) {

    }

    override fun genPatchInternal() {

    }

    @Suppress("UNCHECKED_CAST")
    var s0 : AdaptiveSupportFunction<BT>
        get() = state[0] as AdaptiveSupportFunction<BT>
        set(v) { state[0] = v }

}