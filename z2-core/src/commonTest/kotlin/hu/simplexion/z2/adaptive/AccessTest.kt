/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("unused")
fun Adaptive.accessTest() {
    val a = 12
    accessor { a }
}

fun <T> Adaptive.accessor(
    binding: AdaptiveStateValueBinding<T>? = null,
    @Suppress("UNUSED_PARAMETER") selector: () -> T
) {
    checkNotNull(binding)
    T1(binding.value as Int)
}

class AccessTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        AdaptiveAccessBindingTest(adapter, null, 0).apply {
            create()
            mount(root)
        }

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
                    TraceEvent("AdaptiveAccessor", 3, "before-Create", ""),
                    TraceEvent("AdaptiveAccessor", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveAccessor", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateValueBinding(owner=AdaptiveAccessBindingTest @ 2, indexInState=0, indexInClosure=0 type=kotlin.Int) supportFunction:-1]"),
                    TraceEvent("AdaptiveAccessor", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [AdaptiveStateValueBinding(owner=AdaptiveAccessBindingTest @ 2, indexInState=0, indexInClosure=0 type=kotlin.Int) supportFunction:-1]"),
                    TraceEvent("AdaptiveAccessor", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [AdaptiveStateValueBinding(owner=AdaptiveAccessBindingTest @ 2, indexInState=0, indexInClosure=0 type=kotlin.Int) supportFunction:-1]"),
                    TraceEvent("AdaptiveT1", 4, "before-Create", ""),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12]"),
                    TraceEvent("AdaptiveT1", 4, "after-Create", ""),
                    TraceEvent("AdaptiveAccessor", 3, "after-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Create", ""),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveAccessor", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 4, "before-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveT1", 4, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveAccessor", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("AdaptiveAccessBindingTest", 2, "after-Mount", "bridge: 1")
                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptiveAccessBindingTest(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptiveAccessor(adapter, parent, declarationIndex)
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
                    fragment.setStateVariable(
                        0,
                        AdaptiveStateValueBinding<Int>(
                            this,
                            0,
                            0,
                            AdaptivePropertyMetadata("kotlin.Int"),
                            - 1
                        )
                    )
                }
            }
        }
    }

    override fun genPatchInternal() {

    }

}

class AdaptiveAccessor(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0

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
                    fragment.setStateVariable(0, 12)
                }
            }
        }
    }

    override fun genPatchInternal() {

    }

}