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
 * fun Z2.higherOrderTest() {
 *     higherFun(12) { lowerFunI1 ->
 *         higherFun(lowerFunI1) { lowerFunI2 ->
 *             T1(lowerFunI1 + lowerFunI2)
 *         }
 *     }
 * }
 *
 * fun Z3.higherFun(higherI : Int, lowerFun : Z3.(lowerFunI : Int) -> Unit) {
 *     higherFunInner(higherI*2) { lowerFunInnerI ->
 *         lowerFun(higherI + lowerFunInnerI)
 *     }
 * }
 *
 * fun Z3.higherFunInner(innerI : Int, lowerFunInner : Z3.(lowerFunInnerI : Int) -> Unit) {
 *     lowerFunInner(innerI + 1) // Anonymous 1, Anonymous 3
 * }
 * ```
 */
class HigherOrderTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        HigherOrderTestComponent(adapter, null, 0).apply {
            create()
            mount(root)
        }

        assertEquals(
            "OK", AdaptiveTestAdapter.assert(
                listOf(
                    TraceEvent("HigherOrderTestComponent", 2, "create", ""),
                    TraceEvent("HigherOrderTestComponent", 2, "beforePatchExternal", "closureDirtyMask: 0 state: []"),
                    TraceEvent("HigherOrderTestComponent", 2, "afterPatchExternal", "closureDirtyMask: 0 state: []"),
                    TraceEvent("HigherOrderTestComponent", 2, "beforePatchInternal", "closureDirtyMask: 0 state: []"),
                    TraceEvent("HigherOrderTestComponent", 2, "afterPatchInternal", "closureDirtyMask: 0 state: []"),
                    TraceEvent("HigherFun", 3, "create", ""),
                    TraceEvent("HigherFun", 3, "beforePatchExternal", "closureDirtyMask: 0 state: [null, null]"),
                    TraceEvent("HigherFun", 3, "afterPatchExternal", "closureDirtyMask: 0 state: [12, AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("HigherFun", 3, "beforePatchInternal", "closureDirtyMask: 0 state: [12, AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("HigherFun", 3, "afterPatchInternal", "closureDirtyMask: 0 state: [12, AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("HigherFunInner", 4, "create", ""),
                    TraceEvent("HigherFunInner", 4, "beforePatchExternal", "closureDirtyMask: 0 state: [null, null]"),
                    TraceEvent("HigherFunInner", 4, "afterPatchExternal", "closureDirtyMask: 0 state: [24, AdaptiveFragmentFactory(3,1)]"),
                    TraceEvent("HigherFunInner", 4, "beforePatchInternal", "closureDirtyMask: 0 state: [24, AdaptiveFragmentFactory(3,1)]"),
                    TraceEvent("HigherFunInner", 4, "afterPatchInternal", "closureDirtyMask: 0 state: [24, AdaptiveFragmentFactory(3,1)]"),
                    TraceEvent("AdaptiveAnonymous", 5, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 5, "afterPatchExternal", "closureDirtyMask: 0 state: [25]"),
                    TraceEvent("AdaptiveAnonymous", 6, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 6, "beforePatchExternal", "closureDirtyMask: -4 state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 6, "afterPatchExternal", "closureDirtyMask: -4 state: [37]"),
                    TraceEvent("HigherFun", 7, "create", ""),
                    TraceEvent("HigherFun", 7, "beforePatchExternal", "closureDirtyMask: -1 state: [null, null]"),
                    TraceEvent("HigherFun", 7, "afterPatchExternal", "closureDirtyMask: -1 state: [37, AdaptiveFragmentFactory(2,2)]"),
                    TraceEvent("HigherFun", 7, "beforePatchInternal", "closureDirtyMask: -1 state: [37, AdaptiveFragmentFactory(2,2)]"),
                    TraceEvent("HigherFun", 7, "afterPatchInternal", "closureDirtyMask: -1 state: [37, AdaptiveFragmentFactory(2,2)]"),
                    TraceEvent("HigherFunInner", 8, "create", ""),
                    TraceEvent("HigherFunInner", 8, "beforePatchExternal", "closureDirtyMask: 0 state: [null, null]"),
                    TraceEvent("HigherFunInner", 8, "afterPatchExternal", "closureDirtyMask: 0 state: [74, AdaptiveFragmentFactory(7,1)]"),
                    TraceEvent("HigherFunInner", 8, "beforePatchInternal", "closureDirtyMask: 0 state: [74, AdaptiveFragmentFactory(7,1)]"),
                    TraceEvent("HigherFunInner", 8, "afterPatchInternal", "closureDirtyMask: 0 state: [74, AdaptiveFragmentFactory(7,1)]"),
                    TraceEvent("AdaptiveAnonymous", 9, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 9, "afterPatchExternal", "closureDirtyMask: 0 state: [75]"),
                    TraceEvent("AdaptiveAnonymous", 10, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 10, "beforePatchExternal", "closureDirtyMask: -4 state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 10, "afterPatchExternal", "closureDirtyMask: -4 state: [112]"),
                    TraceEvent("AdaptiveT1", 11, "create", ""),
                    TraceEvent("AdaptiveT1", 11, "beforePatchExternal", "closureDirtyMask: -1 state: [null]"),
                    TraceEvent("AdaptiveT1", 11, "afterPatchExternal", "closureDirtyMask: -1 state: [149]"),
                    TraceEvent("AdaptiveT1", 11, "beforePatchInternal", "closureDirtyMask: -1 state: [149]"),
                    TraceEvent("AdaptiveT1", 11, "afterPatchInternal", "closureDirtyMask: -1 state: [149]"),
                    TraceEvent("HigherOrderTestComponent", 2, "mount", "bridge: 1"),
                    TraceEvent("HigherFun", 3, "mount", "bridge: 1"),
                    TraceEvent("HigherFunInner", 4, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveAnonymous", 5, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveAnonymous", 6, "mount", "bridge: 1"),
                    TraceEvent("HigherFun", 7, "mount", "bridge: 1"),
                    TraceEvent("HigherFunInner", 8, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveAnonymous", 9, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveAnonymous", 10, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 11, "mount", "bridge: 1")
                )
            )
        )
    }
}

class HigherOrderTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, parent, index, 0) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x00 // fragment index: 0, state variable index: 1

    val dependencyMask_1_0 = 0x00 // fragment index: 1, state variable index: 0
    val dependencyMask_1_1 = 0x00 // fragment index: 1, state variable index: 1

    val dependencyMask_2_0 = 0x00 // fragment index: 2, state variable index: 0

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> HigherFun(adapter, parent, declarationIndex)
            1 -> HigherFun(adapter, parent, declarationIndex)
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
                    fragment.setStateVariable(0, 12)
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    fragment.setStateVariable(1, AdaptiveFragmentFactory(this, 1))
                }
            }

            1 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_1_1)) {
                    fragment.setStateVariable(0, fragment.getClosureVariable(0) as Int)
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_1_1)) {
                    fragment.setStateVariable(1, AdaptiveFragmentFactory(this, 2))
                }
            }

            2 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_2_0)) {
                    fragment.setStateVariable(0, (fragment.getClosureVariable(0) as Int) + (fragment.getClosureVariable(1) as Int))
                }
            }

            else -> invalidIndex(fragment.index)
        }
    }
}

class HigherFun(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, parent, index, 2) {

    val higherI
        get() = state[0] as Int

    @Suppress("UNCHECKED_CAST")
    val builder
        get() = state[1] as AdaptiveFragmentFactory<TestNode>

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> HigherFunInner(adapter, parent, declarationIndex)
            1 -> AdaptiveAnonymous(adapter, parent, declarationIndex, 1, builder)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patchDescendant(fragment: AdaptiveFragment<TestNode>) {
        when (fragment.index) {
            0 -> {
                fragment.state[0] = higherI * 2
                fragment.state[1] = AdaptiveFragmentFactory(this, 1)
            }

            1 -> {
                // higherI + lowerFunInnerI
                fragment.state[0] = (fragment.getClosureVariable(0) as Int) + (fragment.getClosureVariableFromLast(0) as Int)
            }

            else -> invalidIndex(fragment.index)
        }
    }
}

class HigherFunInner(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, parent, index, 2) {

    val innerI
        get() = state[0] as Int

    @Suppress("UNCHECKED_CAST")
    val builder
        get() = state[1] as AdaptiveFragmentFactory<TestNode>

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveAnonymous(adapter, parent, declarationIndex, 1, builder)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patchDescendant(fragment: AdaptiveFragment<TestNode>) {
        when (fragment.index) {
            0 -> {
                fragment.state[0] = innerI + 1
            }

            else -> invalidIndex(fragment.index)
        }
    }

}