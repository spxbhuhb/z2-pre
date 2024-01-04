/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.rui.success

import hu.simplexion.z2.rui.Rui
import hu.simplexion.z2.rui.rui
import hu.simplexion.z2.rui.RuiAdapterRegistry
import hu.simplexion.z2.rui.testing.RuiTestAdapter
import hu.simplexion.z2.rui.testing.RuiTestAdapter.TraceEvent
import hu.simplexion.z2.rui.testing.RuiTestAdapterFactory
import hu.simplexion.z2.rui.testing.T0
import hu.simplexion.z2.rui.testing.T1

@Rui
fun WhenBranch(i : Int) {
    when {
        i == 0 -> T0
        i == 1 -> T1(-1)
        else -> T1(i)
    }
}

fun box() : String {

    RuiAdapterRegistry.register(RuiTestAdapterFactory)

    rui {
        WhenBranch(1)
    }

    return RuiTestAdapter.assert(listOf(
    ))
}