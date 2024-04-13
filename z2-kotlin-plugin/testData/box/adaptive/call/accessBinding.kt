/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.AdaptiveStateValueBinding
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.accessTest() {
    val a = 12
    accessor { a }
}

fun <T> Adaptive.accessor(
    binding: AdaptiveStateValueBinding<T>? = null,
    @Suppress("UNUSED_PARAMETER") selector: () -> T
) {
    checkNotNull(binding)
    T1(binding.value as Int)
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        accessTest()
    }

    return AdaptiveTestAdapter.assert(listOf(
        //@formatter:off
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveAccessTest", 3, "before-Create", ""),
        TraceEvent("AdaptiveAccessTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveAccessTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveAccessTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveAccessTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveAccessor", 4, "before-Create", ""),
        TraceEvent("AdaptiveAccessor", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveAccessor", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateValueBinding(3, 0, 0, AdaptivePropertyMetadata(kotlin.Int), -1)]"),
        TraceEvent("AdaptiveAccessor", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateValueBinding(3, 0, 0, AdaptivePropertyMetadata(kotlin.Int), -1)]"),
        TraceEvent("AdaptiveAccessor", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateValueBinding(3, 0, 0, AdaptivePropertyMetadata(kotlin.Int), -1)]"),
        TraceEvent("AdaptiveT1", 5, "before-Create", ""),
        TraceEvent("AdaptiveT1", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
        TraceEvent("AdaptiveT1", 5, "after-Create", ""),
        TraceEvent("AdaptiveAccessor", 4, "after-Create", ""),
        TraceEvent("AdaptiveAccessTest", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveAccessTest", 3, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveAccessor", 4, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveT1", 5, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveAccessor", 4, "after-Mount", "bridge: 1"),
        TraceEvent("AdaptiveAccessTest", 3, "after-Mount", "bridge: 1"),
        TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
        //@formatter:on
    ))
}