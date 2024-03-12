/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapterFactory
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter.TraceEvent
import hu.simplexion.z2.adaptive.testing.T0
import hu.simplexion.z2.adaptive.testing.T1

@Adaptive
fun IfOnly(i : Int) {
    if (i % 2 == 0) {
        T0()
    }
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        IfOnly(5)
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", "init", ),
        TraceEvent("AdaptiveIfOnly", "init", ),
        TraceEvent("AdaptiveWhen", "init", "newBranch:", "-1"),
        TraceEvent("<root>", "create", ),
        TraceEvent("AdaptiveIfOnly", "create", ),
        TraceEvent("AdaptiveWhen", "create", ),
        TraceEvent("<root>", "mount", "bridge", "1"),
        TraceEvent("AdaptiveIfOnly", "mount", "bridge", "1"),
        TraceEvent("AdaptiveWhen", "mount", "bridge:", "1")
    ))
}