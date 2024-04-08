/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.AdaptivePoll.Companion.pollLock
import hu.simplexion.z2.adaptive.testing.*
import hu.simplexion.z2.util.placeholder
import hu.simplexion.z2.util.use
import kotlin.test.Test
import kotlin.test.assertEquals

fun <T> poll(
    @Suppress("UNUSED_PARAMETER")
    pollFun: suspend () -> T
): T {
    placeholder()
}

var counter = 12

@Suppress("unused")
fun Adaptive.pollTest() {
    val i = poll { pollLock.use { counter++ } }
    T1(i)
}

class PollTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        AdaptivePollTest(adapter, null, 0).apply {
            create()
            mount(root)
        }

        while (true) {
            val done = adapter.traceLock.use {
                adapter.traceEvents.lastOrNull()?.let { it.point == "after-Invoke" && it.data.lastOrNull() == "index: 0 result: 14" } ?: false
            }
            if (done) break
        }

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off
                    TraceEvent("AdaptivePollTest", 2, "before-Create", ""),
                    TraceEvent("AdaptivePollTest", 2, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptivePollTest", 2, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
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

                    TraceEvent("AdaptivePollTest", 2, "before-Invoke", "callingFragment: AdaptivePollTest @ 2 index: 0 arguments: []"),
                    TraceEvent("AdaptivePollTest", 2, "after-Invoke", "index: 0 result: 12"),

                    TraceEvent("AdaptivePollTest", 2, "before-Invoke", "callingFragment: AdaptivePollTest @ 2 index: 0 arguments: []"),
                    TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [12]"),
                    TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [0]"),
                    TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [12]"),
                    TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [12]"),
                    TraceEvent("AdaptivePlaceholder", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptivePlaceholder", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptivePollTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptivePollTest", 2, "after-Invoke", "index: 0 result: 13"),

                    TraceEvent("AdaptivePollTest", 2, "before-Invoke", "callingFragment: AdaptivePollTest @ 2 index: 0 arguments: []"),
                    TraceEvent("AdaptivePollTest", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [13]"),
                    TraceEvent("AdaptiveT1", 3, "before-Patch-External", "createMask: 0x00000001 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 3, "after-Patch-External", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
                    TraceEvent("AdaptiveT1", 3, "before-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000001 state: [13]"),
                    TraceEvent("AdaptivePlaceholder", 4, "before-Patch-External", "createMask: 0x00000001 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptivePlaceholder", 4, "after-Patch-External", "createMask: 0x00000001 thisMask: 0xffffffff state: []"),
                    TraceEvent("AdaptiveT1", 3, "after-Patch-Internal", "createMask: 0x00000001 thisMask: 0x00000000 state: [13]"),
                    TraceEvent("AdaptivePollTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
                    TraceEvent("AdaptivePollTest", 2, "after-Invoke", "index: 0 result: 14")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = true)
        )
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
                    pollLock.use {
                        fragment.setStateVariable(0, this.getThisClosureVariable(0))
                    }
                }
            }
        }
    }

    override fun genPatchInternal() {
        if (getThisClosureDirtyMask() == adaptiveInitStateMask) {
            this.setStateVariable(0, 0)
        }
        workers += AdaptivePoll(AdaptiveSupportFunction(this, 0), 0, null, 2)
    }

    @Suppress("RedundantNullableReturnType")
    override fun genInvoke(
        supportFunction: AdaptiveSupportFunction<TestNode>,
        callingFragment: AdaptiveFragment<TestNode>,
        arguments: Array<out Any?>
    ): Any? {

        return when (supportFunction.supportFunctionIndex) {
            0 -> counter++
            else -> invalidIndex(supportFunction.supportFunctionIndex)
        }

    }

}