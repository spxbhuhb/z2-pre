/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.AdaptiveT1
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter.TraceEvent
import hu.simplexion.z2.adaptive.testing.AdaptiveTestBridge
import hu.simplexion.z2.adaptive.testing.TestNode
import kotlin.test.Test
import kotlin.test.assertEquals

class BranchTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        Branch(adapter, null, null, {  }, emptyArray()).apply {
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

        assertEquals(
            "OK", AdaptiveTestAdapter.assert(
                listOf(
                    TraceEvent("Branch", "init"),
                    TraceEvent("AdaptiveWhen", "init", "newBranch:", "0"),
                    TraceEvent("AdaptiveT1", "init"),

                    TraceEvent("Branch", "create"),
                    TraceEvent("AdaptiveWhen", "create"),
                    TraceEvent("AdaptiveT1", "create", "p0:", "11"),

                    TraceEvent("Branch", "mount", "bridge", "1"),
                    TraceEvent("AdaptiveWhen", "mount", "bridge:", "1"),
                    TraceEvent("AdaptiveT1", "mount", "bridge:", "2"),

                    TraceEvent("AdaptiveWhen", "patch", "branch:", "0", "newBranch:", "0"),
                    TraceEvent("AdaptiveT1", "patch", "adaptiveDirty0:", "(size=1, value=0)", "p0:", "11"),

                    TraceEvent("AdaptiveWhen", "patch", "branch:", "0", "newBranch:", "1"),
                    TraceEvent("AdaptiveT1", "unmount", "bridge:", "2"),
                    TraceEvent("AdaptiveT1", "dispose"),
                    TraceEvent("AdaptiveT1", "init"),
                    TraceEvent("AdaptiveT1", "create", "p0:", "22"),
                    TraceEvent("AdaptiveT1", "mount", "bridge:", "2"),

                    TraceEvent("AdaptiveWhen", "patch", "branch:", "1", "newBranch:", "2"),
                    TraceEvent("AdaptiveT1", "unmount", "bridge:", "2"),
                    TraceEvent("AdaptiveT1", "dispose"),

                    TraceEvent("AdaptiveWhen", "patch", "branch:", "2", "newBranch:", "0"),
                    TraceEvent("AdaptiveT1", "init"),
                    TraceEvent("AdaptiveT1", "create", "p0:", "11"),
                    TraceEvent("AdaptiveT1", "mount", "bridge:", "2")
                )
            )
        )

    }

}

@Suppress("unused")
class Branch(
    override val adaptiveAdapter: AdaptiveAdapter<TestNode>,
    override val adaptiveParent: AdaptiveFragment<TestNode>?,
    override val adaptiveClosure: AdaptiveClosure<TestNode>?,
    override val adaptiveExternalPatch: AdaptiveExternalPatchType<TestNode>,
    override val adaptiveState: Array<Any?>
) : AdaptiveGeneratedFragment<TestNode> {

    override val containedFragment: AdaptiveFragment<TestNode>

    var v0: Int = 1

    var adaptiveDirty0 = 0L

    init {
        adaptiveAdapter.trace("Branch", "init")
    }

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
        containedFragment.adaptivePatch()
    }

    fun adaptiveBuilder0(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> =
        AdaptiveT1(adaptiveAdapter, null, parent, ::adaptiveEp0, arrayOf(v0 + 10))

    fun adaptiveBuilder1(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> =
        AdaptiveT1(adaptiveAdapter, null, parent, ::adaptiveEp1, arrayOf(v0 + 20))

    fun adaptiveBuilder2(parent: AdaptiveFragment<TestNode>): AdaptiveFragment<TestNode> =
        AdaptivePlaceholder(adaptiveAdapter, parent)

    /**
     * The return of select us passed to the factory function.
     */
    fun adaptiveExternalPatchSelect(it: AdaptiveFragment<TestNode>) {
        (it as AdaptiveWhen<TestNode>).newBranch =
            when (v0) {
                1 -> 0
                2 -> 1
                else -> 2
            }
    }

    fun adaptiveFactorySelect(parent: AdaptiveFragment<TestNode>, index: Int): AdaptiveFragment<TestNode>? {
        return when (index) {
            - 1 -> null
            0 -> adaptiveBuilder0(parent)
            1 -> adaptiveBuilder1(parent)
            2 -> adaptiveBuilder2(parent)
            else -> throw IllegalArgumentException()
        }
    }

    init {
        containedFragment = AdaptiveWhen(
            adaptiveAdapter,
            null,
            this,
            ::adaptiveExternalPatchSelect,
            ::adaptiveFactorySelect
        )
    }
}