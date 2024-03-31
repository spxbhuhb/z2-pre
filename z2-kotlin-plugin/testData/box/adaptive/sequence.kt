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
        TraceEvent("<root>", 2, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveSequenceTestComponent", 3, "create", ""),
        TraceEvent("AdaptiveSequenceTestComponent", 3, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveSequenceTestComponent", 3, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveSequenceTestComponent", 3, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveSequenceTestComponent", 3, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveSequence", 4, "create", ""),
        TraceEvent("AdaptiveSequence", 4, "beforePatchExternal", "closureDirtyMask: 0 state: null"),
        TraceEvent("AdaptiveSequence", 4, "afterPatchExternal", "closureDirtyMask: 0 state: [1, 2]"),
        TraceEvent("AdaptiveT0", 5, "create", ""),
        TraceEvent("AdaptiveT0", 5, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveT0", 5, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveT0", 5, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveT0", 5, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveT0", 6, "create", ""),
        TraceEvent("AdaptiveT0", 6, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveT0", 6, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveT0", 6, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveT0", 6, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "mount", "bridge: 1"),
        TraceEvent("AdaptiveSequenceTestComponent", 3, "mount", "bridge: 1"),
        TraceEvent("AdaptiveSequence", 4, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT0", 5, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT0", 6, "mount", "bridge: 1")
    ))
}