/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.*
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
            "OK", AdaptiveTestAdapter.assert(
                listOf(
                    TraceEvent("LoopTestComponent", 2, "create", ""),
                    TraceEvent("LoopTestComponent", 2, "beforePatchExternal", "closureDirtyMask: 0 state: [3]"),
                    TraceEvent("LoopTestComponent", 2, "afterPatchExternal", "closureDirtyMask: 0 state: [3]"),
                    TraceEvent("LoopTestComponent", 2, "beforePatchInternal", "closureDirtyMask: 0 state: [3]"),
                    TraceEvent("LoopTestComponent", 2, "afterPatchInternal", "closureDirtyMask: 0 state: [3]"),
                    TraceEvent("AdaptiveLoop", 3, "create", ""),
                    TraceEvent("AdaptiveLoop", 3, "beforePatchExternal", "closureDirtyMask: 0 state: [null,null]"),
                    TraceEvent("AdaptiveLoop", 3, "afterPatchExternal", "closureDirtyMask: 0 state: [IntProgressionIterator,AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("AdaptiveAnonymous", 5, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "beforePatchExternal", "closureDirtyMask: -1 state: [0]"),
                    TraceEvent("AdaptiveAnonymous", 5, "afterPatchExternal", "closureDirtyMask: -1 state: [0]"),
                    TraceEvent("AdaptiveT1", 6, "create", ""),
                    TraceEvent("AdaptiveT1", 6, "beforePatchExternal", "closureDirtyMask: -2 state: [null]"),
                    TraceEvent("AdaptiveT1", 6, "afterPatchExternal", "closureDirtyMask: -2 state: [10]"),
                    TraceEvent("AdaptiveT1", 6, "beforePatchInternal", "closureDirtyMask: -2 state: [10]"),
                    TraceEvent("AdaptiveT1", 6, "afterPatchInternal", "closureDirtyMask: -2 state: [10]"),
                    TraceEvent("AdaptiveAnonymous", 7, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 7, "beforePatchExternal", "closureDirtyMask: -1 state: [1]"),
                    TraceEvent("AdaptiveAnonymous", 7, "afterPatchExternal", "closureDirtyMask: -1 state: [1]"),
                    TraceEvent("AdaptiveT1", 8, "create", ""),
                    TraceEvent("AdaptiveT1", 8, "beforePatchExternal", "closureDirtyMask: -2 state: [null]"),
                    TraceEvent("AdaptiveT1", 8, "afterPatchExternal", "closureDirtyMask: -2 state: [11]"),
                    TraceEvent("AdaptiveT1", 8, "beforePatchInternal", "closureDirtyMask: -2 state: [11]"),
                    TraceEvent("AdaptiveT1", 8, "afterPatchInternal", "closureDirtyMask: -2 state: [11]"),
                    TraceEvent("AdaptiveAnonymous", 9, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "beforePatchExternal", "closureDirtyMask: -1 state: [2]"),
                    TraceEvent("AdaptiveAnonymous", 9, "afterPatchExternal", "closureDirtyMask: -1 state: [2]"),
                    TraceEvent("AdaptiveT1", 10, "create", ""),
                    TraceEvent("AdaptiveT1", 10, "beforePatchExternal", "closureDirtyMask: -2 state: [null]"),
                    TraceEvent("AdaptiveT1", 10, "afterPatchExternal", "closureDirtyMask: -2 state: [12]"),
                    TraceEvent("AdaptiveT1", 10, "beforePatchInternal", "closureDirtyMask: -2 state: [12]"),
                    TraceEvent("AdaptiveT1", 10, "afterPatchInternal", "closureDirtyMask: -2 state: [12]"),
                    TraceEvent("AdaptiveAnonymous", 11, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 11, "beforePatchExternal", "closureDirtyMask: -1 state: [3]"),
                    TraceEvent("AdaptiveAnonymous", 11, "afterPatchExternal", "closureDirtyMask: -1 state: [3]"),
                    TraceEvent("AdaptiveT1", 12, "create", ""),
                    TraceEvent("AdaptiveT1", 12, "beforePatchExternal", "closureDirtyMask: -2 state: [null]"),
                    TraceEvent("AdaptiveT1", 12, "afterPatchExternal", "closureDirtyMask: -2 state: [13]"),
                    TraceEvent("AdaptiveT1", 12, "beforePatchInternal", "closureDirtyMask: -2 state: [13]"),
                    TraceEvent("AdaptiveT1", 12, "afterPatchInternal", "closureDirtyMask: -2 state: [13]"),
                    TraceEvent("LoopTestComponent", 2, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveLoop", 3, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveAnonymous", 5, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 6, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 7, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 8, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 9, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 10, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveAnonymous", 11, "mount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 12, "mount", "bridge: 4")
                )
            )
        )
    }
}

class LoopTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, parent, index, 1) {

    var count : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x02 // fragment index: 0, state variable index: 0
    val dependencyMask_1_0 = 0x02 // fragment index: 1, state variable index: 0

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode>? {
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

    override fun invoke(supportFunction: AdaptiveSupportFunction<TestNode>, vararg arguments: Any?) : Any {
        return when (supportFunction.supportFunctionIndex) {
            0 -> (0..count).iterator()
            else -> invalidIndex(supportFunction.supportFunctionIndex)
        }
    }

}