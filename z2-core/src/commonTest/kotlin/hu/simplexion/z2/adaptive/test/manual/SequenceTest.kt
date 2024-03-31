/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptiveGeneratedFragment
import hu.simplexion.z2.adaptive.AdaptiveSequence
import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * fun sequenceTestComponent() {
 *     T0()
 *     T1(12)
 * }
 */
class SequenceTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        SequenceTestComponent(adapter, null, 0).apply {
            create()
            mount(root)
        }

        assertEquals(
            "OK",
            AdaptiveTestAdapter.assert(
                listOf(
                    TraceEvent("SequenceTestComponent", 2, "create", ""),
                    TraceEvent("AdaptiveSequence", 3, "create", ""),
                    TraceEvent("AdaptiveSequence", 3, "patchExternal", "closureDirtyMask: -1 state: [1, 2]"),
                    TraceEvent("AdaptiveT0", 4, "create", ""),
                    TraceEvent("AdaptiveT0", 4, "patchExternal", "closureDirtyMask: -1 state: []"),
                    TraceEvent("AdaptiveT0", 4, "patchInternal", "closureDirtyMask: -1 state: []"),
                    TraceEvent("AdaptiveT1", 5, "create", ""),
                    TraceEvent("AdaptiveT1", 5, "patchExternal", "closureDirtyMask: -1 state: [12]"),
                    TraceEvent("AdaptiveT1", 5, "patchInternal", "closureDirtyMask: -1 state: [12]"),
                    TraceEvent("SequenceTestComponent", 2, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveSequence", 3, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveT0", 4, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 5, "mount", "bridge: 1")
                )
            )
        )
    }
}

class SequenceTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, parent, index, 0) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0
    val dependencyMask_1_0 = 0x00 // fragment index: 1, state variable index: 0

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveSequence(adapter, parent, declarationIndex)
            1 -> AdaptiveT0(adapter, parent, declarationIndex)
            2 -> AdaptiveT1(adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patchDescendant(fragment: AdaptiveFragment<TestNode>) {

        val closureMask = fragment.getClosureDirtyMask()

        when (fragment.index) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    fragment.setStateVariable(0, intArrayOf(1, 2)) // indices of T0 and T1
                }
            }

            2 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_1_0)) {
                    fragment.setStateVariable(0, 12)
                }
            }
        }
    }

    override fun patchInternal() {
        containedFragment?.patchInternal()
    }
}