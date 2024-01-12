/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui.test.manual

import hu.simplexion.z2.rui.*
import hu.simplexion.z2.rui.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockTest {

    @Test
    fun test() {
        val adapter = RuiTestAdapter()
        val root = RuiTestBridge(1)

        Block(adapter).apply {
            ruiCreate()
            ruiMount(root)
        }

        assertEquals(testResult, adapter.trace.joinToString("\n"))
    }

    val testResult = """
        [ RuiT1                          ]  init                  |  
        [ RuiT1                          ]  create                |  p0: 1
        [ RuiT0                          ]  create                |  
        [ RuiT1                          ]  mount                 |  bridge: 1
        [ RuiT0                          ]  mount                 |  bridge: 1
    """.trimIndent()

}


@Suppress("unused")
class Block(
    override val ruiAdapter: RuiAdapter<TestNode>
) : RuiGeneratedFragment<TestNode> {

    override val ruiScope: RuiFragment<TestNode>? = null
    override val ruiExternalPatch: RuiExternalPathType<TestNode> = { _, _ -> 0L }

    override val containedFragment: RuiFragment<TestNode>

    var v0 = 1

    var ruiDirty0 = 0L

    fun ruiInvalidate0(mask: Long) {
        ruiDirty0 = ruiDirty0 or mask
    }

    fun ruiEp1(it: RuiFragment<TestNode>, scopeMask: Long): Long {
        if (scopeMask and 1 != 0L) return 0L

        it as RuiT1
        if (ruiDirty0 and 1L != 0L) {
            it.p0 = v0
            it.ruiInvalidate0(1)
        }

        return scopeMask
    }

    override fun ruiPatch(dirtyMaskOfScope: Long) {
        val extendedScopeMask = containedFragment.ruiExternalPatch(containedFragment, dirtyMaskOfScope)
        if (extendedScopeMask != 0L) containedFragment.ruiPatch(extendedScopeMask)
        ruiDirty0 = 0L
    }

    init {
        containedFragment = RuiBlock(
            ruiAdapter,
            RuiT1(ruiAdapter, this, ::ruiEp1, v0),
            RuiT0(ruiAdapter, this) { _, _ -> 0L }
        )
    }

}