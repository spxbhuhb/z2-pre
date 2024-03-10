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
import hu.simplexion.z2.adaptive.testing.T1

@Adaptive
fun Variables(i: Int, s: String) {
    val i2 = 12

    T1(0)
    T1(i)
    T1(i2)
    T1(i + i2)
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        Variables(123, "abc")
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("AdaptiveT1", "init", ),
        TraceEvent("AdaptiveT1", "init", ),
        TraceEvent("AdaptiveT1", "init", ),
        TraceEvent("AdaptiveT1", "init", ),
        TraceEvent("AdaptiveT1", "create", "p0:", "0"),
        TraceEvent("AdaptiveT1", "create", "p0:", "123"),
        TraceEvent("AdaptiveT1", "create", "p0:", "12"),
        TraceEvent("AdaptiveT1", "create", "p0:", "135"),
        TraceEvent("AdaptiveT1", "mount", "bridge:", "1"),
        TraceEvent("AdaptiveT1", "mount", "bridge:", "1"),
        TraceEvent("AdaptiveT1", "mount", "bridge:", "1"),
        TraceEvent("AdaptiveT1", "mount", "bridge:", "1")
    ))
}