/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.testing.*
import hu.simplexion.z2.adaptive.worker.poll
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

var counter = 12

@Suppress("unused")
fun Adaptive.pollTest() {
    val i = poll(Duration.ZERO, 2, counter) { counter++ }
    T1(i)
}

@Suppress("OPT_IN_USAGE")
class PollTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        adapter.dispatcher = newSingleThreadContext("test thread")

        runBlocking(adapter.dispatcher) {
            AdaptivePollTest(adapter, null, 0).apply {
                create()
                mount(root)
            }

            adapter.waitFor(Regex.fromLiteral("after-Invoke-Suspend"), Regex.fromLiteral("index: 0 result: 14"))

            assertEquals(
                adapter.expected(
                    listOf(
                        //@formatter:off
                        TraceEvent("AdaptivePollTest", 2, "before-Create", ""),
                        TraceEvent("AdaptivePollTest", 2, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                        TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                        TraceEvent("AdaptivePollTest", 2, "before-Add-Worker", "worker: AdaptivePoll(AdaptiveStateValueBinding(owner=AdaptivePollTest @ 2, indexInState=0, indexInClosure=0 type=kotlin.int) supportFunction:0, interval=0s, repeatLimit=2)"),
                        TraceEvent("AdaptivePollTest", 2, "after-Add-Worker", "worker: AdaptivePoll(AdaptiveStateValueBinding(owner=AdaptivePollTest @ 2, indexInState=0, indexInClosure=0 type=kotlin.int) supportFunction:0, interval=0s, repeatLimit=2)"),
                        TraceEvent("AdaptivePollTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [0]"),
                        TraceEvent("AdaptiveT1", 3, "before-Create", ""),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [0]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [0]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [0]"),
                        TraceEvent("AdaptiveT1", 3, "after-Create", ""),
                        TraceEvent("AdaptivePollTest", 2, "after-Create", ""),
                        TraceEvent("AdaptivePollTest", 2, "before-Mount", "bridge: 1"),
                        TraceEvent("AdaptiveT1", 3, "before-Mount", "bridge: 1"),
                        TraceEvent("AdaptiveT1", 3, "after-Mount", "bridge: 1"),
                        TraceEvent("AdaptivePollTest", 2, "after-Mount", "bridge: 1"),
                        TraceEvent("AdaptivePollTest", 2, "before-Invoke-Suspend", "index: 0 arguments: [AdaptivePollTest @ 2]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Invoke-Suspend", "index: 0 result: 12"),
                        TraceEvent("AdaptivePollTest", 2, "before-Invoke-Suspend", "index: 0 arguments: [AdaptivePollTest @ 2]"),
                        TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [12]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [0]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [12]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [12]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Invoke-Suspend", "index: 0 result: 13"),
                        TraceEvent("AdaptivePollTest", 2, "before-Invoke-Suspend", "index: 0 arguments: [AdaptivePollTest @ 2]"),
                        TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [13]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
                        TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
                        TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [13]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
                        TraceEvent("AdaptivePollTest", 2, "after-Invoke-Suspend", "index: 0 result: 14")
                        //@formatter:on
                    )
                ),
                adapter.actual(dumpCode = false)
            )
        }
    }
}

class AdaptivePollTest(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveT1(adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment<TestNode>) {

        val closureMask = fragment.getCreateClosureDirtyMask()

        when (fragment.index) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    fragment.setStateVariable(0, this.getThisClosureVariable(0))
                }
            }
        }
    }

    override fun genPatchInternal() {
        if (getThisClosureDirtyMask() == adaptiveInitStateMask) {
            this.setStateVariable(
                0,
                poll(
                    Duration.ZERO, 2, 0,
                    AdaptiveStateValueBinding(this, 0, 0, AdaptivePropertyMetadata("kotlin.int"), 0),
                    null
                )
            )
        }
    }

    @Suppress("RedundantNullableReturnType")
    override suspend fun genInvokeSuspend(
        supportFunction: AdaptiveSupportFunction,
        arguments: Array<out Any?>
    ): Any? {

        return when (supportFunction.supportFunctionIndex) {
            0 -> counter++
            else -> invalidIndex(supportFunction.supportFunctionIndex)
        }

    }

}