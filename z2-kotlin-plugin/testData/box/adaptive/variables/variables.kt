/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.Variables(i: Int, s: String) {
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
        TraceEvent("<root>", 2, "create", ""),
        TraceEvent("<root>", 2, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveVariables", 3, "create", ""),
        TraceEvent("AdaptiveVariables", 3, "beforePatchExternal", "closureDirtyMask: 0 state: [null, null, null]"),
        TraceEvent("AdaptiveVariables", 3, "afterPatchExternal", "closureDirtyMask: 0 state: [123, abc, null]"),
        TraceEvent("AdaptiveVariables", 3, "beforePatchInternal", "closureDirtyMask: 0 state: [123, abc, null]"),
        TraceEvent("AdaptiveVariables", 3, "afterPatchInternal", "closureDirtyMask: 0 state: [123, abc, 12]"),
        TraceEvent("AdaptiveSequence", 4, "create", ""),
        TraceEvent("AdaptiveSequence", 4, "beforePatchExternal", "closureDirtyMask: 0 state: null"),
        TraceEvent("AdaptiveSequence", 4, "afterPatchExternal", "closureDirtyMask: 0 state: [1, 2, 3, 4]"),
        TraceEvent("AdaptiveT1", 5, "create", ""),
        TraceEvent("AdaptiveT1", 5, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
        TraceEvent("AdaptiveT1", 5, "afterPatchExternal", "closureDirtyMask: 0 state: [0]"),
        TraceEvent("AdaptiveT1", 5, "beforePatchInternal", "closureDirtyMask: 0 state: [0]"),
        TraceEvent("AdaptiveT1", 5, "afterPatchInternal", "closureDirtyMask: 0 state: [0]"),
        TraceEvent("AdaptiveT1", 6, "create", ""),
        TraceEvent("AdaptiveT1", 6, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
        TraceEvent("AdaptiveT1", 6, "afterPatchExternal", "closureDirtyMask: 0 state: [123]"),
        TraceEvent("AdaptiveT1", 6, "beforePatchInternal", "closureDirtyMask: 0 state: [123]"),
        TraceEvent("AdaptiveT1", 6, "afterPatchInternal", "closureDirtyMask: 0 state: [123]"),
        TraceEvent("AdaptiveT1", 7, "create", ""),
        TraceEvent("AdaptiveT1", 7, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
        TraceEvent("AdaptiveT1", 7, "afterPatchExternal", "closureDirtyMask: 0 state: [12]"),
        TraceEvent("AdaptiveT1", 7, "beforePatchInternal", "closureDirtyMask: 0 state: [12]"),
        TraceEvent("AdaptiveT1", 7, "afterPatchInternal", "closureDirtyMask: 0 state: [12]"),
        TraceEvent("AdaptiveT1", 8, "create", ""),
        TraceEvent("AdaptiveT1", 8, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
        TraceEvent("AdaptiveT1", 8, "afterPatchExternal", "closureDirtyMask: 0 state: [135]"),
        TraceEvent("AdaptiveT1", 8, "beforePatchInternal", "closureDirtyMask: 0 state: [135]"),
        TraceEvent("AdaptiveT1", 8, "afterPatchInternal", "closureDirtyMask: 0 state: [135]"),
        TraceEvent("<root>", 2, "mount", "bridge: 1"),
        TraceEvent("AdaptiveVariables", 3, "mount", "bridge: 1"),
        TraceEvent("AdaptiveSequence", 4, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 6, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 7, "mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 8, "mount", "bridge: 1")
    ))
}