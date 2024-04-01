/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestBridge
import hu.simplexion.z2.adaptive.testing.TestNode
import hu.simplexion.z2.adaptive.testing.TraceEvent
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * fun supportFunctionTest(i : Int, supportFun : (i : Int) -> Unit) {
 *     supportFun(i)
 * }
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
            assertEquals(23, it.state[0])
        }

        assertEquals(
            adapter.expected(
                listOf(
                    TraceEvent("SupportFunctionTestComponent", 2, "create", ""),
                    TraceEvent("SupportFunctionTestComponent", 2, "beforePatchExternal", "closureDirtyMask: 0 state: [null]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "afterPatchExternal", "closureDirtyMask: 0 state: [null]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "beforePatchInternal", "closureDirtyMask: 0 state: [null]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "afterPatchInternal", "closureDirtyMask: 0 state: [null]"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "create", ""),
                    TraceEvent("SupportFunctionInnerComponent", 3, "beforePatchExternal", "closureDirtyMask: 0 state: [null, null]"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "afterPatchExternal", "closureDirtyMask: 0 state: [12, AdaptiveSupportFunction(2, 0)]"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "beforePatchInternal", "closureDirtyMask: 0 state: [12, AdaptiveSupportFunction(2, 0)]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "invoke", "index: 0 arguments: [12]"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "afterPatchInternal", "closureDirtyMask: 0 state: [12, AdaptiveSupportFunction(2, 0)]"),
                    TraceEvent("SupportFunctionTestComponent", 2, "mount", "bridge: 1"),
                    TraceEvent("SupportFunctionInnerComponent", 3, "mount", "bridge: 1")
                )
            ),
            adapter.actual()
        )
    }
}

class SupportFunctionTestComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x00 // fragment index: 0, state variable index: 0
    val dependencyMask_0_1 = 0x00 // fragment index: 1, state variable index: 1

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode>? {

        val fragment = when (declarationIndex) {
            0 -> SupportFunctionInnerComponent(adapter, parent, declarationIndex)
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
                    fragment.setStateVariable(1, AdaptiveSupportFunction(fragment, 0))
                }
            }
        }
    }

    @Suppress("RedundantNullableReturnType")
    override fun invoke(supportFunction: AdaptiveSupportFunction<TestNode>, arguments: Array<out Any?>): Any? {
        trace(supportFunction, arguments)
        return when (supportFunction.supportFunctionIndex) {
            0 -> { state[0] = (arguments[0] as Int) + 11 } // this is a bit dirty, but good enough for testing
            else -> invalidIndex(supportFunction.supportFunctionIndex)
        }
    }
}

class SupportFunctionInnerComponent(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveGeneratedFragment<TestNode>(adapter, parent, index, 2) {

    override fun build(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode>? {
        return null
    }

    override fun patchDescendant(fragment: AdaptiveFragment<TestNode>) {
        shouldNotRun()
    }

    override fun patchInternal() {
        patchInternalStart()
        (getThisClosureVariable(1) as AdaptiveSupportFunction<*>).invoke(getThisClosureVariable(0))
        patchInternalEnd()
    }
}