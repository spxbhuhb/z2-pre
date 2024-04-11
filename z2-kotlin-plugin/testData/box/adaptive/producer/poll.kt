/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.AdaptiveStateValueBinding
import hu.simplexion.z2.adaptive.testing.*
import hu.simplexion.z2.adaptive.worker.*
import kotlinx.coroutines.*
import kotlin.time.Duration

var counter = 12

fun Adaptive.pollTest() {
    val i = poll(Duration.ZERO, 2, counter) {
        counter = counter + 1
        counter
    }
    T1(i)
}

@Suppress("OPT_IN_USAGE")
fun box(): String {

    val adapter = AdaptiveTestAdapter()
    adapter.dispatcher = newSingleThreadContext("test thread")

    try {

        adaptive(adapter) {
            pollTest()
        }

        runBlocking(adapter.dispatcher) {
            adapter.waitFor(Regex.fromLiteral("after-Invoke-Suspend"), Regex.fromLiteral("index: 0 result: 15"))
        }

    } finally {
        (adapter.dispatcher as CloseableCoroutineDispatcher).close()
    }

    return AdaptiveTestAdapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
            TraceEvent("AdaptivePollTest", 3, "before-Create", ""),
            TraceEvent("AdaptivePollTest", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptivePollTest", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptivePollTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent(
                "AdaptivePollTest",
                3,
                "before-Add-Worker",
                "worker: AdaptivePoll(AdaptiveStateValueBinding(owner=AdaptivePollTest @ 3, indexInState=0, indexInClosure=0 type=kotlin.Int) supportFunction:0, interval=0s, repeatLimit=2)"
            ),
            TraceEvent(
                "AdaptivePollTest",
                3,
                "after-Add-Worker",
                "worker: AdaptivePoll(AdaptiveStateValueBinding(owner=AdaptivePollTest @ 3, indexInState=0, indexInClosure=0 type=kotlin.Int) supportFunction:0, interval=0s, repeatLimit=2)"
            ),
            TraceEvent("AdaptivePollTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 4, "before-Create", ""),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 4, "after-Create", ""),
            TraceEvent("AdaptivePollTest", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptivePollTest", 3, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT1", 4, "before-Mount", "bridge: 1"),
            TraceEvent("AdaptiveT1", 4, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptivePollTest", 3, "after-Mount", "bridge: 1"),
            TraceEvent("<root>", 2, "after-Mount", "bridge: 1"),
            TraceEvent("AdaptivePollTest", 3, "before-Invoke-Suspend", "index: 0 arguments: [AdaptivePollTest @ 3]"),
            TraceEvent("AdaptivePollTest", 3, "after-Invoke-Suspend", "index: 0 result: 13"),
            TraceEvent("AdaptivePollTest", 3, "before-Invoke-Suspend", "index: 0 arguments: [AdaptivePollTest @ 3]"),
            TraceEvent("AdaptivePollTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [13]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
            TraceEvent("AdaptivePlaceholder", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptivePlaceholder", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [13]"),
            TraceEvent("AdaptivePollTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
            TraceEvent("AdaptivePollTest", 3, "after-Invoke-Suspend", "index: 0 result: 14"),
            TraceEvent("AdaptivePollTest", 3, "before-Invoke-Suspend", "index: 0 arguments: [AdaptivePollTest @ 3]"),
            TraceEvent("AdaptivePollTest", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [14]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [13]"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [14]"),
            TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [14]"),
            TraceEvent("AdaptivePlaceholder", 5, "before-Patch-External", "createMask: 0x00000001 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptivePlaceholder", 5, "after-Patch-External", "createMask: 0x00000001 thisMask: 0xffffffff state: []"),
            TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [14]"),
            TraceEvent("AdaptivePollTest", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [14]"),
            TraceEvent("AdaptivePollTest", 3, "after-Invoke-Suspend", "index: 0 result: 15")
        )
    )
}