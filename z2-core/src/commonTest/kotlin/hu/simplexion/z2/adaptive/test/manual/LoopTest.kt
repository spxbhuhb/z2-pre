/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptiveFragmentFactory
import hu.simplexion.z2.adaptive.AdaptiveLoop
import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ```kotlin
 * fun loop(count : Int) {
 *     for (i in 0 .. count) {
 *         T1(i + 10)
 *     }
 * }
 * ```
 */
class LoopTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        LoopTestComponent(adapter, null, 0).apply {
            state[0] = 3 // count
            create()
            mount(root)
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("LoopTestComponent", 2, "create", ""),
                    TraceEvent("LoopTestComponent", 2, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [3]"),
                    TraceEvent("LoopTestComponent", 2, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [3]"),
                    TraceEvent("LoopTestComponent", 2, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [3]"),
                    TraceEvent("LoopTestComponent", 2, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [3]"),
                    TraceEvent("AdaptiveLoop", 3, "create", ""),
                    TraceEvent("AdaptiveLoop", 3, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
                    TraceEvent("AdaptiveLoop", 3, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [IntProgressionIterator,AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveLoop", 3, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [IntProgressionIterator,AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveAnonymous", 5, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "beforePatchExternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveAnonymous", 5, "afterPatchExternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveAnonymous", 5, "beforePatchInternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [0]"),
                    TraceEvent("AdaptiveAnonymous", 5, "afterPatchInternal", "createMask: 0xffffffff thisMask: 0x00000000 state: [0]"),
                    TraceEvent("AdaptiveT1", 6, "create", ""),
                    TraceEvent("AdaptiveT1", 6, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 6, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [10]"),
                    TraceEvent("AdaptiveT1", 6, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [10]"),
                    TraceEvent("AdaptiveT1", 6, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [10]"),
                    TraceEvent("AdaptiveAnonymous", 5, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 6, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 9, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "beforePatchExternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveAnonymous", 9, "afterPatchExternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveAnonymous", 9, "beforePatchInternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [1]"),
                    TraceEvent("AdaptiveAnonymous", 9, "afterPatchInternal", "createMask: 0xffffffff thisMask: 0x00000000 state: [1]"),
                    TraceEvent("AdaptiveT1", 10, "create", ""),
                    TraceEvent("AdaptiveT1", 10, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 10, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [11]"),
                    TraceEvent("AdaptiveT1", 10, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [11]"),
                    TraceEvent("AdaptiveT1", 10, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [11]"),
                    TraceEvent("AdaptiveAnonymous", 9, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 10, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 13, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 13, "beforePatchExternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
                    TraceEvent("AdaptiveAnonymous", 13, "afterPatchExternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
                    TraceEvent("AdaptiveAnonymous", 13, "beforePatchInternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [2]"),
                    TraceEvent("AdaptiveAnonymous", 13, "afterPatchInternal", "createMask: 0xffffffff thisMask: 0x00000000 state: [2]"),
                    TraceEvent("AdaptiveT1", 14, "create", ""),
                    TraceEvent("AdaptiveT1", 14, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 14, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 14, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 14, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveAnonymous", 13, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 14, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 17, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 17, "beforePatchExternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveAnonymous", 17, "afterPatchExternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveAnonymous", 17, "beforePatchInternal", "createMask: 0xffffffff thisMask: 0xffffffff state: [3]"),
                    TraceEvent("AdaptiveAnonymous", 17, "afterPatchInternal", "createMask: 0xffffffff thisMask: 0x00000000 state: [3]"),
                    TraceEvent("AdaptiveT1", 18, "create", ""),
                    TraceEvent("AdaptiveT1", 18, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 18, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
                    TraceEvent("AdaptiveT1", 18, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [13]"),
                    TraceEvent("AdaptiveT1", 18, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
                    TraceEvent("AdaptiveAnonymous", 17, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 18, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveLoop", 3, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [IntProgressionIterator,AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("LoopTestComponent", 2, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveLoop", 3, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveAnonymous", 5, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 6, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 9, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 10, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 13, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 14, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 17, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 18, "mount", "bridge: 4")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class LoopTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    var count : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x02 // fragment index: 0, state variable index: 0
    val dependencyMask_1_0 = 0x02 // fragment index: 1, state variable index: 0

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveLoop<TestNode, Int>(adapter, parent, declarationIndex)
            1 -> AdaptiveT1(adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patchDescendant(fragment: AdaptiveFragment<TestNode>) {

        val closureMask = fragment.getCreateClosureDirtyMask()

        when (fragment.index) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    fragment.setStateVariable(0, (0 .. count).iterator())
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    fragment.setStateVariable(1, AdaptiveFragmentFactory(this, 1))
                }
            }
            1 -> {
                // T1.createClosure is [ (count), (i) ]
                if (fragment.haveToPatch(closureMask, dependencyMask_1_0)) {
                    fragment.setStateVariable(0, (fragment.getCreateClosureVariable(1) as Int) + 10)
                }
            }
            else -> invalidIndex(fragment.index)
        }
    }

    override fun generatedPatchInternal() {

    }

}