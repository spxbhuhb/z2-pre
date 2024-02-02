/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.assertEquals

class ForLoopTest {

    // not implemented yet @Test
    fun test() {
        val adapter = RuiTestAdapter()
        val root = RuiTestBridge(1)

        ForLoop(adapter, null).apply {
            ruiCreate()
            ruiMount(root)
        }

        assertEquals(testResult, adapter.trace.joinToString("\n"))
    }

    val testResult = """
        ...
    """.trimIndent()

}

@Suppress("unused")
class ForLoop(
    override val ruiAdapter: RuiAdapter<TestNode>,
    override val ruiParent: RuiFragment<TestNode>?
) : RuiGeneratedFragment<TestNode> {

    override val ruiClosure: RuiClosure<TestNode>? = null
    override val ruiExternalPatch: RuiExternalPatchType<TestNode> = {  }

    override val containedFragment: RuiFragment<TestNode>

    var v0 = 1

    var ruiDirty0 = 0L

    fun ruiInvalidate0(mask: Long) {
        ruiDirty0 = ruiDirty0 or mask
    }

    fun ruiEp1(it: RuiFragment<TestNode>) {
        it as RuiT1
        if (ruiDirty0 and 1L != 0L) {
            it.p0 = v0
            it.ruiInvalidate0(1)
        }
    }

    override fun ruiPatch() {
        containedFragment.ruiExternalPatch(containedFragment)
    }

    fun ruiIterator0() = IntRange(0, 10).iterator()

    fun ruiBuilderT1(parent : RuiFragment<TestNode>) : RuiFragment<TestNode> {
        return RuiT1(ruiAdapter, null, parent, ::ruiEp1, v0)
    }

    fun ruiBuilderT0(parent : RuiFragment<TestNode>) : RuiFragment<TestNode> {
        return RuiT0(ruiAdapter, null, this) {  }
    }

    fun ruiBuilder0() =
        RuiSequence(
            ruiAdapter,
            null,
            this,
            ::ruiBuilderT1,
            ::ruiBuilderT0
        )

    init {
        containedFragment = RuiLoop(
            ruiAdapter,
            null,
            this,
            ::ruiIterator0,
            ::ruiBuilder0
        )
    }
}