/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        Block(adapter, null).apply {
            adaptiveCreate()
            adaptiveMount(root)
        }

        assertEquals(testResult, adapter.trace.joinToString("\n"))
    }

    val testResult = """
        [ AdaptiveT1                          ]  init                  |  
        [ AdaptiveT1                          ]  create                |  p0: 1
        [ AdaptiveT0                          ]  create                |  
        [ AdaptiveT1                          ]  mount                 |  bridge: 1
        [ AdaptiveT0                          ]  mount                 |  bridge: 1
    """.trimIndent()

}


@Suppress("unused")
class Block(
    override val adaptiveAdapter: AdaptiveAdapter<TestNode>,
    override val adaptiveParent: AdaptiveFragment<TestNode>?
) : AdaptiveGeneratedFragment<TestNode> {

    override val adaptiveClosure: AdaptiveClosure<TestNode>? = null
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode> = {  }

    override val containedFragment: AdaptiveFragment<TestNode>

    var v0 = 1

    var adaptiveDirty0 = 0L

    fun adaptiveInvalidate0(mask: Long) {
        adaptiveDirty0 = adaptiveDirty0 or mask
    }

    fun adaptiveEp1(it: AdaptiveFragment<TestNode>) {
        it as AdaptiveT1
        if (adaptiveDirty0 and 1L != 0L) {
            it.p0 = v0
            it.adaptiveInvalidate0(1)
        }
    }

    override fun adaptivePatch() {
        containedFragment.adaptiveExternalPatch(containedFragment)
        adaptiveDirty0 = 0L
    }

    fun adaptiveBuilderT1(parent : AdaptiveFragment<TestNode>) : AdaptiveFragment<TestNode> {
        return AdaptiveT1(adaptiveAdapter, null, parent, ::adaptiveEp1, v0)
    }

    fun adaptiveBuilderT0(parent : AdaptiveFragment<TestNode>) : AdaptiveFragment<TestNode> {
        return AdaptiveT0(adaptiveAdapter, null, this) {  }
    }

    fun adaptiveBuilderSequence(parent : AdaptiveFragment<TestNode>) : AdaptiveFragment<TestNode> {
        val tmp = AdaptiveSequence(adaptiveAdapter, null, parent)

        tmp.add(adaptiveBuilderT1(tmp))
        tmp.add(adaptiveBuilderT0(tmp))

        return tmp
    }

    init {
        containedFragment = adaptiveBuilderSequence(this)
    }

}