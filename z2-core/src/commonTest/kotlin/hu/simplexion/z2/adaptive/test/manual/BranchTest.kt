/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.test.manual

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.RuiT1
import hu.simplexion.z2.adaptive.testing.RuiTestAdapter
import hu.simplexion.z2.adaptive.testing.RuiTestBridge
import hu.simplexion.z2.adaptive.testing.TestNode
import kotlin.test.Test

class BranchTest {

    @Test
    fun test() {
        val adapter = RuiTestAdapter()
        val root = RuiTestBridge(1)

        Branch(adapter, null).apply {
            ruiCreate()
            ruiMount(root)

            fun v(value: Int) {
                v0 = value
                ruiInvalidate0(1)
                ruiPatch()
            }

            v(1)
            v(2)
            v(3)
            v(1)
        }

        // FIXME assertEquals(testResult, adapter.trace.joinToString("\n"))

    }

    val testResult = """
        [ RuiT1                          ]  init                  |  
        [ RuiT1                          ]  create                |  p0: 11
        [ RuiT1                          ]  mount                 |  bridge: 2
        [ RuiT1                          ]  patch                 |  ruiDirty0: 0 p0: 11
        [ RuiT1                          ]  unmount               |  bridge: 2
        [ RuiT1                          ]  dispose               |  
        [ RuiT1                          ]  init                  |  
        [ RuiT1                          ]  create                |  p0: 22
        [ RuiT1                          ]  mount                 |  bridge: 2
        [ RuiT1                          ]  unmount               |  bridge: 2
        [ RuiT1                          ]  dispose               |  
        [ RuiT1                          ]  init                  |  
        [ RuiT1                          ]  create                |  p0: 11
        [ RuiT1                          ]  mount                 |  bridge: 2
    """.trimIndent()

}

@Suppress("unused")
class Branch(
    override val ruiAdapter: RuiAdapter<TestNode>,
    override val ruiParent: RuiFragment<TestNode>?
) : RuiGeneratedFragment<TestNode> {

    override val ruiClosure: RuiClosure<TestNode>? = null
    override val ruiExternalPatch: RuiExternalPatchType<TestNode> = {  }

    override val containedFragment: RuiFragment<TestNode>

    var v0: Int = 1

    var ruiDirty0 = 0L

    fun ruiInvalidate0(mask: Long) {
        ruiDirty0 = ruiDirty0 or mask
    }

    fun ruiEp0(it: RuiFragment<TestNode>) {
        it as RuiT1
        if (ruiDirty0 and 1L != 0L) {
            it.p0 = v0 + 10
            ruiInvalidate0(1)
        }
    }

    fun ruiEp1(it: RuiFragment<TestNode>) {
        it as RuiT1
        if (ruiDirty0 and 1L != 0L) {
            it.p0 = v0 + 20
            ruiInvalidate0(1)
        }
    }

    override fun ruiPatch() {
        containedFragment.ruiExternalPatch(containedFragment)
    }

    fun ruiBranch0(): RuiFragment<TestNode> = RuiT1(ruiAdapter, null, this, ::ruiEp0, v0 + 10)
    fun ruiBranch1(): RuiFragment<TestNode> = RuiT1(ruiAdapter, null, this, ::ruiEp1, v0 + 20)
    fun ruiBranch2(): RuiFragment<TestNode> = RuiPlaceholder(ruiAdapter, this)

    fun ruiSelect(): Int =
        when (v0) {
            1 -> 0 // index in RuiSelect.fragments
            2 -> 1
            else -> 2
        }

    init {
        containedFragment = RuiSelect(
            ruiAdapter,
            null,
            this,
            ::ruiSelect,
            ::ruiBranch0,
            ::ruiBranch1,
            ::ruiBranch2
        )
    }
}