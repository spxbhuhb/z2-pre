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
import hu.simplexion.z2.rui.testing.T1

@Rui
fun Variables(i: Int, s: String) {
//    val i2 = 12

//    T1(0)
    T1(i)
//    T1(i2)
}

fun box() : String {

    RuiAdapterRegistry.register(RuiTestAdapterFactory)

    rui {
        Variables(123, "abc")
    }

    return RuiTestAdapter.assert(listOf(
    ))
}