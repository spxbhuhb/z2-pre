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
        TraceEvent("<root>", 2, "create", ""),
        TraceEvent("<root>", 2, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveBasic", 3, "create", ""),
        TraceEvent("AdaptiveBasic", 3, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveBasic", 3, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveBasic", 3, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveBasic", 3, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveT1", 4, "create", ""),
        TraceEvent("AdaptiveT1", 4, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
        TraceEvent("AdaptiveT1", 4, "afterPatchExternal", "closureDirtyMask: 0 state: [12]"),
        TraceEvent("AdaptiveT1", 4, "beforePatchInternal", "closureDirtyMask: 0 state: [12]"),
        TraceEvent("AdaptiveT1", 4, "afterPatchInternal", "closureDirtyMask: 0 state: [12]"),
        TraceEvent("<root>", 2, "mount", "bridge: 1"),
        TraceEvent("AdaptiveBasic", 3, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 4, "mount", "bridge: 1")
    ))
}