/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.Basic() {
    T1(12)
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        Basic()
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", 2, "create"),
        TraceEvent("AdaptiveBasic", 3, "create"),
        TraceEvent("AdaptiveT1", 4, "create", "p0:", "12"),
        TraceEvent("<root>", 2, "mount", "bridge", "1"),
        TraceEvent("AdaptiveBasic", 3, "mount", "bridge", "1"),
        TraceEvent("AdaptiveT1", 4, "mount", "bridge:", "1")
    ))
}