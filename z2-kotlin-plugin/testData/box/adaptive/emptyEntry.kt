/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {

    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", 2, "create", ""),
        TraceEvent("<root>", 2, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("AdaptiveSequence", 3, "create", ""),
        TraceEvent("AdaptiveSequence", 3, "beforePatchExternal", "closureDirtyMask: 0 state: null"),
        TraceEvent("AdaptiveSequence", 3, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
        TraceEvent("<root>", 2, "mount", "bridge: 1"),
        TraceEvent("AdaptiveSequence", 3, "mount", "bridge: 1")
    ))
}