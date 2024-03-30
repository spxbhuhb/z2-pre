/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*


fun Adaptive.Empty() {

}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        Empty()
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", 2, "create"),
        TraceEvent("AdaptiveEmpty", 3, "create"),
        TraceEvent("AdaptiveSequence", 4, "create"),
        TraceEvent("<root>", 2, "mount", "bridge", "1"),
        TraceEvent("AdaptiveEmpty", 3, "mount", "bridge", "1"),
        TraceEvent("AdaptiveSequence", 4, "mount", "bridge:", "1")
    ))
}