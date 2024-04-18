/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.IfOnlyTrue(i: Int) {
    if (i % 2 == 0) {
        T0()
    }
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        IfOnlyTrue(6)
    }

    return AdaptiveTestAdapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveIfOnlyTrue", 3, "before-Create", ""),
            TraceEvent("AdaptiveIfOnlyTrue", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveIfOnlyTrue", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [6]"),
            TraceEvent("AdaptiveIfOnlyTrue", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [6]"),
            TraceEvent("AdaptiveIfOnlyTrue", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [6]"),
            TraceEvent("AdaptiveSelect", 4, "before-Create", ""),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, -1]"),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, -1]"),
            TraceEvent("AdaptiveSelect", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [1, -1]"),
            TraceEvent("AdaptiveT0", 6, "before-Create", ""),
            TraceEvent("AdaptiveT0", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT0", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptiveT0", 6, "after-Create", ""),
            TraceEvent("AdaptiveSelect", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [1, 1]"),
            TraceEvent("AdaptiveSelect", 4, "after-Create", ""),
            TraceEvent("AdaptiveIfOnlyTrue", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveIfOnlyTrue", 3, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveSelect", 4, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT0", 6, "before-Mount", "bridge: 5"),
            TraceEvent("AdaptiveT0", 6, "after-Mount", "bridge: 5"),
            TraceEvent("AdaptiveSelect", 4, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptiveIfOnlyTrue", 3, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
        )
    )
}