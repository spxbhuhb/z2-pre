/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.AdaptiveT1
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestBridge
import hu.simplexion.z2.adaptive.testing.TestNode
import kotlin.test.Test

class BranchTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        Branch(adapter, null).apply {
            adaptiveCreate()
            adaptiveMount(root)

            fun v(value: Int) {
                v0 = value
                adaptiveInvalidate0(1)
                adaptivePatch()
            }

            v(1)
            v(2)
            v(3)
            v(1)
        }

        // FIXME assertEquals(testResult, adapter.trace.joinToString("\n"))

    }

    val testResult = """
        [ AdaptiveT1                          ]  init                  |  
        [ AdaptiveT1                          ]  create                |  p0: 11
        [ AdaptiveT1                          ]  mount                 |  bridge: 2
        [ AdaptiveT1                          ]  patch                 |  adaptiveDirty0: 0 p0: 11
        [ AdaptiveT1                          ]  unmount               |  bridge: 2
        [ AdaptiveT1                          ]  dispose               |  
        [ AdaptiveT1                          ]  init                  |  
        [ AdaptiveT1                          ]  create                |  p0: 22
        [ AdaptiveT1                          ]  mount                 |  bridge: 2
        [ AdaptiveT1                          ]  unmount               |  bridge: 2
        [ AdaptiveT1                          ]  dispose               |  
        [ AdaptiveT1                          ]  init                  |  
        [ AdaptiveT1                          ]  create                |  p0: 11
        [ AdaptiveT1                          ]  mount                 |  bridge: 2
    """.trimIndent()

}

@Suppress("unused")
class Branch(
    override val adaptiveAdapter: AdaptiveAdapter<TestNode>,
    override val adaptiveParent: AdaptiveFragment<TestNode>?
) : AdaptiveGeneratedFragment<TestNode> {

    override val adaptiveClosure: AdaptiveClosure<TestNode>? = null
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode> = {  }

    override val containedFragment: AdaptiveFragment<TestNode>

    var v0: Int = 1

    var adaptiveDirty0 = 0L

    fun adaptiveInvalidate0(mask: Long) {
        adaptiveDirty0 = adaptiveDirty0 or mask
    }

    fun adaptiveEp0(it: AdaptiveFragment<TestNode>) {
        it as AdaptiveT1
        if (adaptiveDirty0 and 1L != 0L) {
            it.p0 = v0 + 10
            adaptiveInvalidate0(1)
        }
    }

    fun adaptiveEp1(it: AdaptiveFragment<TestNode>) {
        it as AdaptiveT1
        if (adaptiveDirty0 and 1L != 0L) {
            it.p0 = v0 + 20
            adaptiveInvalidate0(1)
        }
    }

    override fun adaptivePatch() {
        containedFragment.adaptiveExternalPatch(containedFragment)
    }

    fun adaptiveBranch0(): AdaptiveFragment<TestNode> = AdaptiveT1(adaptiveAdapter, null, this, ::adaptiveEp0, v0 + 10)
    fun adaptiveBranch1(): AdaptiveFragment<TestNode> = AdaptiveT1(adaptiveAdapter, null, this, ::adaptiveEp1, v0 + 20)
    fun adaptiveBranch2(): AdaptiveFragment<TestNode> = AdaptivePlaceholder(adaptiveAdapter, this)

    fun adaptiveSelect(): Int =
        when (v0) {
            1 -> 0 // index in AdaptiveSelect.fragments
            2 -> 1
            else -> 2
        }

    init {
        containedFragment = AdaptiveWhen(
            adaptiveAdapter,
            null,
            this,
            ::adaptiveSelect,
            ::adaptiveBranch0,
            ::adaptiveBranch1,
            ::adaptiveBranch2
        )
    }
}