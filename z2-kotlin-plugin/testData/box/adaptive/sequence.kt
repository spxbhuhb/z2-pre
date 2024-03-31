/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.SequenceTestComponent() {
    T0()
    T0()
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        SequenceTestComponent()
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", 2, "create", ""),
        TraceEvent("AdaptiveSequenceTestComponent", 3, "create", ""),
        TraceEvent("AdaptiveSequence", 4, "create", ""),
        TraceEvent("AdaptiveSequence", 4, "patchExternal", "closureDirtyMask: -1 state: [1, 2]"),
        TraceEvent("AdaptiveT0", 5, "create", ""),
        TraceEvent("AdaptiveT0", 5, "patchExternal", "closureDirtyMask: -1 state: []"),
        TraceEvent("AdaptiveT0", 5, "patchInternal", "closureDirtyMask: -1 state: []"),
        TraceEvent("AdaptiveT0", 6, "create", ""),
        TraceEvent("AdaptiveT0", 6, "patchExternal", "closureDirtyMask: -1 state: []"),
        TraceEvent("AdaptiveT0", 6, "patchInternal", "closureDirtyMask: -1 state: []"),
        TraceEvent("<root>", 2, "mount", "bridge: 1"),
        TraceEvent("AdaptiveSequenceTestComponent", 3, "mount", "bridge: 1"),
        TraceEvent("AdaptiveSequence", 4, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT0", 5, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT0", 6, "mount", "bridge: 1")
    ))
}