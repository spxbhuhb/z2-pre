/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter.TraceEvent
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapterFactory
import hu.simplexion.z2.adaptive.testing.T0

@Adaptive
fun Sequence() {
    T0()
    T0()
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        Sequence()
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("AdaptiveT0", "create", ),
        TraceEvent("AdaptiveT0", "create", ),
        TraceEvent("AdaptiveT0", "mount", "bridge:", "1"),
        TraceEvent("AdaptiveT0", "mount", "bridge:", "1")
    ))
}