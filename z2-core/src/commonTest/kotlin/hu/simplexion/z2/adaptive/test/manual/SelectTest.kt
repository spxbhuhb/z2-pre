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
            v0 = 0 // start with empty

            create()
            mount(root)

            fun v(value: Int) {
                v0 = value
                dirtyMask = 1
                patchInternal()
            }

            // even: + 10  odd: + 20
            v(1) // replace empty with 21
            v(2) // replace 21 with 12
            v(3) // replace 12 with 23
            v(1) // patch 23 to 21
            v(0) // replace 21 with empty
        }

        assertEquals(
            "OK", AdaptiveTestAdapter.assert(
                listOf(
                    TraceEvent("SelectTestComponent", 2, "create", ""),

                    TraceEvent("AdaptiveSelect", 3, "create", ""),
                    TraceEvent("AdaptiveSelect", 3, "patchExternal", "closureDirtyMask: 0 state: [-1]"),

                    TraceEvent("SelectTestComponent", 2, "mount", "bridge: 1"),
                    TraceEvent("AdaptiveSelect", 3, "mount", "bridge: 1"),

                    TraceEvent("SelectTestComponent", 2, "patchInternal", "closureDirtyMask: 0 state: [1]"),
                    TraceEvent("AdaptiveSelect", 3, "patchExternal", "closureDirtyMask: 1 state: [2]"),
                    TraceEvent("AdaptiveSelect", 3, "patchInternal", "closureDirtyMask: 1 state: [2]"),
                    TraceEvent("AdaptiveT1", 5, "create", ""),
                    TraceEvent("AdaptiveT1", 5, "patchExternal", "closureDirtyMask: 1 state: [21]"),
                    TraceEvent("AdaptiveT1", 5, "patchInternal", "closureDirtyMask: 1 state: [21]"),
                    TraceEvent("AdaptiveT1", 5, "mount", "bridge: 4"),

                    TraceEvent("SelectTestComponent", 2, "patchInternal", "closureDirtyMask: 0 state: [2]"),
                    TraceEvent("AdaptiveSelect", 3, "patchExternal", "closureDirtyMask: 1 state: [1]"),
                    TraceEvent("AdaptiveSelect", 3, "patchInternal", "closureDirtyMask: 1 state: [1]"),
                    TraceEvent("AdaptiveT1", 5, "unmount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 5, "dispose", ""),
                    TraceEvent("AdaptiveT1", 6, "create", ""),
                    TraceEvent("AdaptiveT1", 6, "patchExternal", "closureDirtyMask: 1 state: [12]"),
                    TraceEvent("AdaptiveT1", 6, "patchInternal", "closureDirtyMask: 1 state: [12]"),
                    TraceEvent("AdaptiveT1", 6, "mount", "bridge: 4"),

                    TraceEvent("SelectTestComponent", 2, "patchInternal", "closureDirtyMask: 0 state: [3]"),
                    TraceEvent("AdaptiveSelect", 3, "patchExternal", "closureDirtyMask: 1 state: [2]"),
                    TraceEvent("AdaptiveSelect", 3, "patchInternal", "closureDirtyMask: 1 state: [2]"),
                    TraceEvent("AdaptiveT1", 6, "unmount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 6, "dispose", ""),
                    TraceEvent("AdaptiveT1", 7, "create", ""),
                    TraceEvent("AdaptiveT1", 7, "patchExternal", "closureDirtyMask: 1 state: [23]"),
                    TraceEvent("AdaptiveT1", 7, "patchInternal", "closureDirtyMask: 1 state: [23]"),
                    TraceEvent("AdaptiveT1", 7, "mount", "bridge: 4"),

                    TraceEvent("SelectTestComponent", 2, "patchInternal", "closureDirtyMask: 0 state: [1]"),
                    TraceEvent("AdaptiveSelect", 3, "patchExternal", "closureDirtyMask: 1 state: [2]"),
                    TraceEvent("AdaptiveSelect", 3, "patchInternal", "closureDirtyMask: 1 state: [2]"),
                    TraceEvent("AdaptiveT1", 7, "patchInternal", "closureDirtyMask: 1 state: [23]"),

                    TraceEvent("SelectTestComponent", 2, "patchInternal", "closureDirtyMask: 0 state: [0]"),
                    TraceEvent("AdaptiveSelect", 3, "patchExternal", "closureDirtyMask: 1 state: [-1]"),
                    TraceEvent("AdaptiveSelect", 3, "patchInternal", "closureDirtyMask: 1 state: [-1]"),
                    TraceEvent("AdaptiveT1", 7, "unmount", "bridge: 4"),
                    TraceEvent("AdaptiveT1", 7, "dispose", "")
                )
            )
        )
    }

}

class SelectTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, parent, index, 1) {

    var v0: Int
        get() = state[0] as Int
        set(v) {
            setStateVariable(0, v)
        }

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0
    val dependencyMask_1_0 = 0x01 // fragment index: 1, state variable index: 0
    val dependencyMask_2_0 = 0x01 // fragment index: 2, state variable index: 0

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

    override fun patchDescendant(fragment: AdaptiveFragment<TestNode>) {

        val closureMask = fragment.getClosureDirtyMask()

        when (fragment.index) {
            0 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_0_0)) {
                    fragment.setStateVariable(
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
                if (fragment.haveToPatch(closureMask, dependencyMask_1_0)) {
                    fragment.setStateVariable(
                        0,
                        v0 + 10
                    )
                }
            }

            2 -> {
                if (fragment.haveToPatch(closureMask, dependencyMask_2_0)) {
                    fragment.setStateVariable(
                        0,
                        v0 + 20
                    )
                }
            }
        }
    }

}