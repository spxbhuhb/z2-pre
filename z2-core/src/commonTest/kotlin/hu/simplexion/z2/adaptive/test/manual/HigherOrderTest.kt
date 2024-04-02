/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveAnonymous
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptiveFragmentFactory
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
            adapter.expected(
                listOf(
                    TraceEvent("HigherOrderTestComponent", 2, "create", ""),
                    TraceEvent("HigherOrderTestComponent", 2, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("HigherOrderTestComponent", 2, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("HigherOrderTestComponent", 2, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
                    TraceEvent("HigherOrderTestComponent", 2, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
                    TraceEvent("HigherFun", 3, "create", ""),
                    TraceEvent("HigherFun", 3, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("HigherFun", 3, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("HigherFun", 3, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("HigherFun", 3, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12, AdaptiveFragmentFactory(2,1)]"),
                    TraceEvent("HigherFunInner", 4, "create", ""),
                    TraceEvent("HigherFunInner", 4, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("HigherFunInner", 4, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [24, AdaptiveFragmentFactory(3,1)]"),
                    TraceEvent("HigherFunInner", 4, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [24, AdaptiveFragmentFactory(3,1)]"),
                    TraceEvent("HigherFunInner", 4, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [24, AdaptiveFragmentFactory(3,1)]"),
                    TraceEvent("AdaptiveAnonymous", 5, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 5, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 5, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [25]"),
                    TraceEvent("AdaptiveAnonymous", 5, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [25]"),
                    TraceEvent("AdaptiveAnonymous", 5, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [25]"),
                    TraceEvent("AdaptiveAnonymous", 6, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 6, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 6, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [37]"),
                    TraceEvent("AdaptiveAnonymous", 6, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [37]"),
                    TraceEvent("AdaptiveAnonymous", 6, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [37]"),
                    TraceEvent("HigherFun", 7, "create", ""),
                    TraceEvent("HigherFun", 7, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("HigherFun", 7, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [37, AdaptiveFragmentFactory(2,2)]"),
                    TraceEvent("HigherFun", 7, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [37, AdaptiveFragmentFactory(2,2)]"),
                    TraceEvent("HigherFun", 7, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [37, AdaptiveFragmentFactory(2,2)]"),
                    TraceEvent("HigherFunInner", 8, "create", ""),
                    TraceEvent("HigherFunInner", 8, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("HigherFunInner", 8, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [74, AdaptiveFragmentFactory(7,1)]"),
                    TraceEvent("HigherFunInner", 8, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [74, AdaptiveFragmentFactory(7,1)]"),
                    TraceEvent("HigherFunInner", 8, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [74, AdaptiveFragmentFactory(7,1)]"),
                    TraceEvent("AdaptiveAnonymous", 9, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 9, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 9, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [75]"),
                    TraceEvent("AdaptiveAnonymous", 9, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [75]"),
                    TraceEvent("AdaptiveAnonymous", 9, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [75]"),
                    TraceEvent("AdaptiveAnonymous", 10, "create", ""),
                    TraceEvent("AdaptiveAnonymous", 10, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAnonymous", 10, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [112]"),
                    TraceEvent("AdaptiveAnonymous", 10, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [112]"),
                    TraceEvent("AdaptiveAnonymous", 10, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [112]"),
                    TraceEvent("AdaptiveT1", 11, "create", ""),
                    TraceEvent("AdaptiveT1", 11, "beforePatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 11, "afterPatchExternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [149]"),
                    TraceEvent("AdaptiveT1", 11, "beforePatchInternal", "createMask: 0x00000000 thisMask: 0xffffffff state: [149]"),
                    TraceEvent("AdaptiveT1", 11, "afterPatchInternal", "createMask: 0x00000000 thisMask: 0x00000000 state: [149]"),
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
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class HigherOrderTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 0) {

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
        val closureMask = fragment.getCreateClosureDirtyMask()

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
                if (fragment.haveToPatch(closureMask, dependencyMask_1_0)) {
                    fragment.setStateVariable(0, fragment.getCreateClosureVariable(0) as Int)
                }
                if (fragment.haveToPatch(closureMask, dependencyMask_1_1)) {
                    fragment.setStateVariable(1, AdaptiveFragmentFactory(this, 2))
                }
            }

            2 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_2_0)) {
                    fragment.setStateVariable(0, (fragment.getCreateClosureVariable(0) as Int) + (fragment.getCreateClosureVariable(1) as Int))
                }
            }

            else -> invalidIndex(fragment.index)
        }
    }

    override fun generatedPatchInternal() {

    }
}

class HigherFun(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 2) {

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
                // TODO haveToPatch
                fragment.setStateVariable(0, higherI * 2)
                fragment.setStateVariable(1, AdaptiveFragmentFactory(this, 1))
            }

            1 -> {
                // TODO haveToPatch
                // higherI + lowerFunInnerI
                fragment.setStateVariable(0, (fragment.getCreateClosureVariable(0) as Int) + (fragment.getCreateClosureVariable(2) as Int))
            }

            else -> invalidIndex(fragment.index)
        }
    }

    override fun generatedPatchInternal() {

    }

}

class HigherFunInner(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 2) {

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

    override fun generatedPatchInternal() {

    }

}