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
fun ForLoop(items : List<Int>) {
    for (item in items) {
        T1(item)
    }
}

fun box() : String {

    RuiAdapterRegistry.register(RuiTestAdapterFactory)

    rui {
        ForLoop(listOf(1,2,3))
    }

    return RuiTestAdapter.assert(listOf(
    ))
}