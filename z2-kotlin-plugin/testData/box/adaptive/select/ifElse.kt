/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.IfElse(i : Int) {
    if (i % 2 == 0) {
        T0()
    } else {
        T1(i)
    }
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        IfElse(5)
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", 2, "create", ""),
        TraceEvent("<root>", 2, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveIfElse", 3, "create", ""),
        TraceEvent("AdaptiveIfElse", 3, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
        TraceEvent("AdaptiveIfElse", 3, "afterPatchExternal", "closureDirtyMask: 0 state: [5]"),
        TraceEvent("AdaptiveIfElse", 3, "beforePatchInternal", "closureDirtyMask: 0 state: [5]"),
        TraceEvent("AdaptiveIfElse", 3, "afterPatchInternal", "closureDirtyMask: 0 state: [5]"),
        TraceEvent("AdaptiveSelect", 4, "create", ""),
        TraceEvent("AdaptiveSelect", 4, "beforePatchExternal", "closureDirtyMask: 0 state: [null, -1]"),
        TraceEvent("AdaptiveSelect", 4, "afterPatchExternal", "closureDirtyMask: 0 state: [2, -1]"),
        TraceEvent("AdaptiveSelect", 4, "beforePatchInternal", "closureDirtyMask: 0 state: [2, -1]"),
        TraceEvent("AdaptiveT1", 6, "create", ""),
        TraceEvent("AdaptiveT1", 6, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
        TraceEvent("AdaptiveT1", 6, "afterPatchExternal", "closureDirtyMask: 0 state: [5]"),
        TraceEvent("AdaptiveT1", 6, "beforePatchInternal", "closureDirtyMask: 0 state: [5]"),
        TraceEvent("AdaptiveT1", 6, "afterPatchInternal", "closureDirtyMask: 0 state: [5]"),
        TraceEvent("AdaptiveSelect", 4, "afterPatchInternal", "closureDirtyMask: 0 state: [2, 2]"),
        TraceEvent("<root>", 2, "mount", "bridge: 1"),
        TraceEvent("AdaptiveIfElse", 3, "mount", "bridge: 1"),
        TraceEvent("AdaptiveSelect", 4, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 6, "mount", "bridge: 5")
    ))
}