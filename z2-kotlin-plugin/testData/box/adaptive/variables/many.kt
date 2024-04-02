/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.ManyVariables(
    i0: Int,
    i1: Int,
    i2: Int,
    i3: Int,
    i4: Int,
    i5: Int,
    i6: Int,
    i7: Int,
    i8: Int,
    i9: Int,
    i10: Int,
    i11: Int,
    i12: Int,
    i13: Int,
    i14: Int,
    i15: Int,
    i16: Int,
    i17: Int,
    i18: Int,
    i19: Int,
    i20: Int,
    i21: Int,
    i22: Int,
    i23: Int,
    i24: Int,
    i25: Int,
    i26: Int,
    i27: Int,
    i28: Int,
    i29: Int
) {
    T1(i12)
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        ManyVariables(
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29
        )
    }

    return AdaptiveTestAdapter.assert(
        listOf(
            TraceEvent("<root>", 2, "create", ""),
            TraceEvent("<root>", 2, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
            TraceEvent("<root>", 2, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
            TraceEvent("<root>", 2, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
            TraceEvent("<root>", 2, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
            TraceEvent("AdaptiveManyVariables", 3, "create", ""),
            TraceEvent("AdaptiveManyVariables", 3, "beforePatchExternal", "closureDirtyMask: 0 state: [null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]"),
            TraceEvent("AdaptiveManyVariables", 3, "afterPatchExternal", "closureDirtyMask: 0 state: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]"),
            TraceEvent("AdaptiveManyVariables", 3, "beforePatchInternal", "closureDirtyMask: 0 state: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]"),
            TraceEvent("AdaptiveManyVariables", 3, "afterPatchInternal", "closureDirtyMask: 0 state: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]"),
            TraceEvent("AdaptiveT1", 4, "create", ""),
            TraceEvent("AdaptiveT1", 4, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
            TraceEvent("AdaptiveT1", 4, "afterPatchExternal", "closureDirtyMask: 0 state: [12]"),
            TraceEvent("AdaptiveT1", 4, "beforePatchInternal", "closureDirtyMask: 0 state: [12]"),
            TraceEvent("AdaptiveT1", 4, "afterPatchInternal", "closureDirtyMask: 0 state: [12]"),
            TraceEvent("<root>", 2, "mount", "bridge: 1"),
            TraceEvent("AdaptiveManyVariables", 3, "mount", "bridge: 1"),
            TraceEvent("AdaptiveT1", 4, "mount", "bridge: 1")
        )
    )
}