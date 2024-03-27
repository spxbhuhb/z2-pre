/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptiveGeneratedFragment
import hu.simplexion.z2.adaptive.AdaptiveSelect
import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ```kotlin
 * fun selectTestComponent(v0 : Int) {
 *     when {
 *         v0 == 0 -> Unit
 *         v0 % 2 == 0 -> T1(v0 + 10)
 *         else -> T1(v0 + 20)
 *     }
 * }
 * ```
 */
class SelectTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        SelectTestComponent(adapter, null, 0).apply {
            v0 = 0

            create()
            mount(root)

            fun v(value: Int) {
                v0 = value
                dirtyMask = 1
                patch()
            }

            // even: + 10  odd: + 20
            v(1) // replace empty with 21
            v(2) // replace 21 with 12
            v(3) // replace 12 with 23
            v(1) // patch 23 to 21
            v(0) // replace 21 with empty
        }

        assertEquals(
            adapter.actual(),
            adapter.expected(
                listOf(
                    TraceEvent("SelectTestComponent", 2, "create"),
                    TraceEvent("AdaptiveSelect", 3, "create"),

                    TraceEvent("SelectTestComponent", 2, "mount", "bridge", "1"),
                    TraceEvent("AdaptiveSelect", 3, "mount", "bridge:", "1"),

                    TraceEvent("SelectTestComponent", 2, "patch"),
                    TraceEvent("AdaptiveSelect", 3, "patch", "shownBranch:", "-1", "stateBranch:", "2"),
                    TraceEvent("AdaptiveT1", 5, "create", "p0:", "21"),
                    TraceEvent("AdaptiveT1", 5, "mount", "bridge:", "4"),

                    TraceEvent("SelectTestComponent", 2, "patch"),
                    TraceEvent("AdaptiveSelect", 3, "patch", "shownBranch:", "2", "stateBranch:", "1"),
                    TraceEvent("AdaptiveT1", 5, "unmount", "bridge:", "4"),
                    TraceEvent("AdaptiveT1", 5, "dispose"),
                    TraceEvent("AdaptiveT1", 6, "create", "p0:", "12"),
                    TraceEvent("AdaptiveT1", 6, "mount", "bridge:", "4"),

                    TraceEvent("SelectTestComponent", 2, "patch"),
                    TraceEvent("AdaptiveSelect", 3, "patch", "shownBranch:", "1", "stateBranch:", "2"),
                    TraceEvent("AdaptiveT1", 6, "unmount", "bridge:", "4"),
                    TraceEvent("AdaptiveT1", 6, "dispose"),
                    TraceEvent("AdaptiveT1", 7, "create", "p0:", "23"),
                    TraceEvent("AdaptiveT1", 7, "mount", "bridge:", "4"),

                    TraceEvent("SelectTestComponent", 2, "patch"),
                    TraceEvent("AdaptiveSelect", 3, "patch", "shownBranch:", "2", "stateBranch:", "2"),
                    TraceEvent("AdaptiveT1", 7, "patch", "dirtyMask:", "1", "p0:", "21"),

                    TraceEvent("SelectTestComponent", 2, "patch"),
                    TraceEvent("AdaptiveSelect", 3, "patch", "shownBranch:", "2", "stateBranch:", "-1"),
                    TraceEvent("AdaptiveT1", 7, "unmount", "bridge:", "4"),
                    TraceEvent("AdaptiveT1", 7, "dispose")
                )
            )
        )
    }

}

class SelectTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    override val parent: AdaptiveFragment<TestNode>?,
    override val index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, 1) {

    var v0: Int
        get() = state[0] as Int
        set(v) {
            set(0, v)
        }

    val closureMask_0_0 = 0x01 // fragment index: 0, state variable index: 0
    val closureMask_1_0 = 0x01 // fragment index: 1, state variable index: 0
    val closureMask_2_0 = 0x01 // fragment index: 2, state variable index: 0

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        val fragment = when (declarationIndex) {
            0 -> AdaptiveSelect(adapter, parent, declarationIndex)
            1 -> AdaptiveT1(adapter, parent, declarationIndex)
            2 -> AdaptiveT1(adapter, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

    override fun patch(fragment: AdaptiveFragment<TestNode>) {

        val closureMask = fragment.createClosure?.closureMask() ?: 0

        when (fragment.index) {
            0 -> {
                if (closureMask and closureMask_0_0 != 0) {
                    fragment.set(
                        0,
                        when {
                            v0 == 0 -> - 1
                            v0 % 2 == 0 -> 1
                            else -> 2
                        }
                    )
                }
            }

            1 -> {
                if (closureMask and closureMask_1_0 != 0) {
                    fragment.set(
                        0,
                        v0 + 10
                    )
                }
            }

            2 -> {
                if (closureMask and closureMask_2_0 != 0) {
                    fragment.set(
                        0,
                        v0 + 20
                    )
                }
            }
        }
    }

}