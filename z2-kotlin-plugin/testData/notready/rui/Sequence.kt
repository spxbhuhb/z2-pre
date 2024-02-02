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
import hu.simplexion.z2.adaptive.testing.T0

@Rui
fun Sequence() {
    T0()
    T0()
}

fun box() : String {

    RuiAdapterRegistry.register(RuiTestAdapterFactory)

    rui {
        Sequence()
    }

    return RuiTestAdapter.assert(listOf(
        TraceEvent("RuiT0", "create", ),
        TraceEvent("RuiT0", "create", ),
        TraceEvent("RuiT0", "mount", "bridge:", "1"),
        TraceEvent("RuiT0", "mount", "bridge:", "1")
    ))
}