/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptivePlaceholder
import hu.simplexion.z2.adaptive.AdaptiveSupportFunction
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestBridge
import hu.simplexion.z2.adaptive.testing.TestNode
import hu.simplexion.z2.adaptive.testing.TraceEvent
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ```kotlin
 * fun supportFunctionTest() {
 *     val i = 13
 *     supportFunctionInnerComponent(12) { i = 11 + it + i }
 * }
 *
 * fun supportFunctionInnerComponent(i : Int, supportFun : (i : Int) -> Unit) {
 *     supportFun(i)
 * }
 * ```
 */
class SupportFunctionTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        SupportFunctionTestComponent(adapter, null, 0).apply {
            create()
            mount(root)
        }.also {
            assertEquals(11 + 12 + 13, it.state[0])
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("SupportFunctionTestComponent", 2, "before-Create", ""),
                    TraceEvent("SupportFunctionTestComponent", 2, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [13]"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "before-Create", ""),
                    TraceEvent("SupportFunctionInnerComponent", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null, null]"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, AdaptiveSupportFunction(2, 0)]"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [12, AdaptiveSupportFunction(2, 0)]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "before-Invoke", "index: 0 arguments: [12]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000001 state: [36]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [36]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "after-Invoke", "index: 0 result: kotlin.Unit"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [12, AdaptiveSupportFunction(2, 0)]"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "after-Create", ""),
                    TraceEvent("SupportFunctionTestComponent", 2, "after-Create", ""),
                    TraceEvent("SupportFunctionTestComponent", 2, "before-Mount", "bridge: 1"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "before-Mount", "bridge: 1"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "after-Mount", "bridge: 1"),
                    TraceEvent("SupportFunctionTestComponent", 2, "after-Mount", "bridge: 1")
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class SupportFunctionTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x00 // fragment index: 1, state variable index: 1

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> SupportFunctionInnerComponent(adapter, parent, declarationIndex)
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
                if (fragment.haveToPatch(closureMask, dependencyMask_0_1)) {
                    fragment.setStateVariable(1, AdaptiveSupportFunction(this, 0))
                }
            }
        }
    }

    override fun genPatchInternal() {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 0)) {
            setStateVariable(0, 13)
        }
    }

    @Suppress("RedundantNullableReturnType")
    override fun genInvoke(
        supportFunction: AdaptiveSupportFunction<TestNode>,
        callingFragment: AdaptiveFragment<TestNode>,
        arguments: Array<out Any?>
    ): Any? {

        val fragment = supportFunction.declaringFragment

        return when (supportFunction.supportFunctionIndex) {
            0 -> {
                fragment.setStateVariable(
                    0,
                    11 + (arguments[0] as Int) + (fragment.getThisClosureVariable(0) as Int)
                )
            }
            else -> invalidIndex(supportFunction.supportFunctionIndex)
        }
    }
}

class SupportFunctionInnerComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 2) {

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {
        return AdaptivePlaceholder(adapter, this, -1)
    }

    override fun genPatchInternal() {
        (getThisClosureVariable(1) as AdaptiveSupportFunction<*>).invoke(this, getThisClosureVariable(0))
    }

}