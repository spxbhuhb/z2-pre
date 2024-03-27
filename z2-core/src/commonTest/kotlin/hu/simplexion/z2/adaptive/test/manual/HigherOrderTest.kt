/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
                    TraceEvent("HigherOrderTestComponent", 2, "create"),
                    TraceEvent("HigherFun", 3, "create"),
                    TraceEvent("HigherFunInner", 4, "create"),
                    TraceEvent("AdaptiveAnonymous", 5, "create"),
                    TraceEvent("AdaptiveAnonymous", 6, "create"),
                    TraceEvent("HigherFun", 7, "create"),
                    TraceEvent("HigherFunInner", 8, "create"),
                    TraceEvent("AdaptiveAnonymous", 9, "create"),
                    TraceEvent("AdaptiveAnonymous", 10, "create"),
                    TraceEvent("AdaptiveT1", 11, "create", "p0:", "149"), // <-- this is the important line
                    TraceEvent("HigherOrderTestComponent", 2, "mount", "bridge", "1"),
                    TraceEvent("HigherFun", 3, "mount", "bridge", "1"),
                    TraceEvent("HigherFunInner", 4, "mount", "bridge", "1"),
                    TraceEvent("AdaptiveAnonymous", 5, "mount", "bridge", "1"),
                    TraceEvent("AdaptiveAnonymous", 6, "mount", "bridge", "1"),
                    TraceEvent("HigherFun", 7, "mount", "bridge", "1"),
                    TraceEvent("HigherFunInner", 8, "mount", "bridge", "1"),
                    TraceEvent("AdaptiveAnonymous", 9, "mount", "bridge", "1"),
                    TraceEvent("AdaptiveAnonymous", 10, "mount", "bridge", "1"),
                    TraceEvent("AdaptiveT1", 11, "mount", "bridge:", "1")
                )
            )
        )
    }
}

class HigherOrderTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    override val parent: AdaptiveFragment<TestNode>?,
    override val index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, 0) {

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

    override fun patch(fragment: AdaptiveFragment<TestNode>) {
        when (fragment.index) {
            0 -> {
                fragment.state[0] = 12
                fragment.state[1] = AdaptiveFragmentFactory(this, 1)
            }
            1 -> {
                fragment.state[0] = (fragment.getClosureVariableFromLast(0) as Int)
                fragment.state[1] = AdaptiveFragmentFactory(this, 2)
            }
            2 -> {
                fragment.state[0] = (fragment.getClosureVariable(0) as Int) + (fragment.getClosureVariableFromLast(0) as Int)
            }
            else -> invalidIndex(fragment.index)
        }
    }
}

class HigherFun(
    adapter: AdaptiveAdapter<TestNode>,
    override val parent: AdaptiveFragment<TestNode>?,
    override val index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, 2) {

    val higherI
        get() = state[0] as Int

    @Suppress("UNCHECKED_CAST")
    val builder
        get() = state[1] as AdaptiveFragmentFactory<TestNode>

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> HigherFunInner(adapter, parent, declarationIndex)
            1 -> AdaptiveAnonymous(adapter, parent, declarationIndex, builder, 1)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patch(fragment: AdaptiveFragment<TestNode>) {
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
    override val parent: AdaptiveFragment<TestNode>?,
    override val index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, 2) {

    val innerI
        get() = state[0] as Int

    @Suppress("UNCHECKED_CAST")
    val builder
        get() = state[1] as AdaptiveFragmentFactory<TestNode>

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveAnonymous(adapter, parent, declarationIndex, builder, 1)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patch(fragment: AdaptiveFragment<TestNode>) {
        when (fragment.index) {
            0 -> {
                fragment.state[0] = innerI + 1
            }
            else -> invalidIndex(fragment.index)
        }
    }

}