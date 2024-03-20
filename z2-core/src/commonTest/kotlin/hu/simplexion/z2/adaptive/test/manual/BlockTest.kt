/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.*
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter.TraceEvent
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        Block(adapter, null, null, emptyArray()).apply {
            adaptiveCreate()
            adaptiveMount(root)
        }

        assertEquals(
            "OK",
            AdaptiveTestAdapter.assert(
                listOf(
                    TraceEvent("Block", "init"),
                    TraceEvent("AdaptiveSequence", "init"),
                    TraceEvent("AdaptiveT1", "init"),
                    TraceEvent("Block", "create"),
                    TraceEvent("AdaptiveSequence", "create"),
                    TraceEvent("AdaptiveT1", "create", "p0:", "1"),
                    TraceEvent("AdaptiveT0", "create"),
                    TraceEvent("Block", "mount", "bridge", "1"),
                    TraceEvent("AdaptiveSequence", "mount", "bridge:", "1"),
                    TraceEvent("AdaptiveT1", "mount", "bridge:", "1"),
                    TraceEvent("AdaptiveT0", "mount", "bridge:", "1")
                )
            )
        )
    }
}


@Suppress("unused")
class Block(
    override val adaptiveAdapter: AdaptiveAdapter<TestNode>,
    override val adaptiveParent: AdaptiveFragment<TestNode>?,
    override val adaptiveClosure: AdaptiveClosure<TestNode>?,
    override val adaptiveState: Array<Any?>
) : AdaptiveGeneratedFragment<TestNode> {

    override val adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode> = { }

    override val containedFragment: AdaptiveFragment<TestNode>

    var v0 = 1

    var adaptiveDirty0 = 0L

    init {
        adaptiveAdapter.trace("Block", "init")
    }

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

    fun adaptiveBuilderT1(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> {
        return AdaptiveT1(adaptiveAdapter, null, parent, ::adaptiveEp1, arrayOf(v0))
    }

    fun adaptiveBuilderT0(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> {
        return AdaptiveT0(adaptiveAdapter, null, this, {}, emptyArray())
    }

    fun adaptiveBuilderSequence(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> {
        val tmp = AdaptiveSequence(adaptiveAdapter, null, parent)

        tmp.add(adaptiveBuilderT1(tmp))
        tmp.add(adaptiveBuilderT0(tmp))

        return tmp
    }

    init {
        containedFragment = adaptiveBuilderSequence(this)
    }

}