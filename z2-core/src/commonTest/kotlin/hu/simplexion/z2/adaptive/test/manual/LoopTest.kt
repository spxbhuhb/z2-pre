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
                    TraceEvent("LoopTestComponent", 2, "create"),
                    TraceEvent("AdaptiveLoop", 3, "create"),
                    TraceEvent("AdaptiveAnonymous", 5, "create"),

                    TraceEvent("AdaptiveT1", 6, "create", "p0:", "10"),
                    TraceEvent("AdaptiveAnonymous", 7, "create"),

                    TraceEvent("AdaptiveT1", 8, "create", "p0:", "11"),
                    TraceEvent("AdaptiveAnonymous", 9, "create"),

                    TraceEvent("AdaptiveT1", 10, "create", "p0:", "12"),
                    TraceEvent("AdaptiveAnonymous", 11, "create"),

                    TraceEvent("AdaptiveT1", 12, "create", "p0:", "13"),
                    TraceEvent("LoopTestComponent", 2, "mount", "bridge", "1"),

                    TraceEvent("AdaptiveLoop", 3, "mount", "bridge:", "1"),

                    TraceEvent("AdaptiveAnonymous", 5, "mount", "bridge", "4"),
                    TraceEvent("AdaptiveT1", 6, "mount", "bridge:", "4"),

                    TraceEvent("AdaptiveAnonymous", 7, "mount", "bridge", "4"),
                    TraceEvent("AdaptiveT1", 8, "mount", "bridge:", "4"),

                    TraceEvent("AdaptiveAnonymous", 9, "mount", "bridge", "4"),
                    TraceEvent("AdaptiveT1", 10, "mount", "bridge:", "4"),

                    TraceEvent("AdaptiveAnonymous", 11, "mount", "bridge", "4"),
                    TraceEvent("AdaptiveT1", 12, "mount", "bridge:", "4")
                )
            )
        )
    }
}

class LoopTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    override val parent: AdaptiveFragment<TestNode>?,
    override val index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, 1) {

    var count : Int
        get() = state[0] as Int
        set(v) { state[0] = v }

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveLoop<TestNode, Int>(adapter, parent, declarationIndex)
            1 -> AdaptiveT1(adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patch(fragment: AdaptiveFragment<TestNode>) {
        when (fragment.index) {
            0 -> {
                // if (thisClosure.isDirty(count_closureMask)) {
                fragment.state[0] = (0 .. count).iterator()
                fragment.state[1] = AdaptiveFragmentFactory(this, 1)
                //     fragment.thisClosure.makeDirty(count_closureMask))
                // }
            }
            1 -> {
                // T1.createClosure is [ (count), (i) ]
                // getVariableFromLast accesses the state of the last component in the closure
                fragment.state[0] = (fragment.getClosureVariableFromLast(0) as Int) + 10
            }
            else -> invalidIndex(fragment.index)
        }
    }

    override fun invoke(supportFunction: AdaptiveSupportFunction<TestNode>, vararg arguments: Any?) : Any {
        return when (supportFunction.index) {
            0 -> (0..count).iterator()
            else -> invalidIndex(supportFunction.index)
        }
    }

}