/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Rui
import hu.simplexion.z2.adaptive.rui
import hu.simplexion.z2.adaptive.RuiAdapterRegistry
import hu.simplexion.z2.adaptive.testing.RuiTestAdapter
import hu.simplexion.z2.adaptive.testing.RuiTestAdapter.TraceEvent
import hu.simplexion.z2.adaptive.testing.RuiTestAdapterFactory
import hu.simplexion.z2.adaptive.testing.T1

@Rui
fun Variables(i: Int, s: String) {
    val i2 = 12

    T1(0)
    T1(i)
    T1(i2)
    T1(i + i2)
}

fun box() : String {

    RuiAdapterRegistry.register(RuiTestAdapterFactory)

    rui {
        Variables(123, "abc")
    }

    return RuiTestAdapter.assert(listOf(
        TraceEvent("RuiT1", "init", ),
        TraceEvent("RuiT1", "init", ),
        TraceEvent("RuiT1", "init", ),
        TraceEvent("RuiT1", "init", ),
        TraceEvent("RuiT1", "create", "p0:", "0"),
        TraceEvent("RuiT1", "create", "p0:", "123"),
        TraceEvent("RuiT1", "create", "p0:", "12"),
        TraceEvent("RuiT1", "create", "p0:", "135"),
        TraceEvent("RuiT1", "mount", "bridge:", "1"),
        TraceEvent("RuiT1", "mount", "bridge:", "1"),
        TraceEvent("RuiT1", "mount", "bridge:", "1"),
        TraceEvent("RuiT1", "mount", "bridge:", "1")
    ))
}