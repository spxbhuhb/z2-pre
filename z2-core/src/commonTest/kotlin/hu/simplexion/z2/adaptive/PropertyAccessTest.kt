/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.adaptive.binding.AdaptivePropertyMetadata
import hu.simplexion.z2.adaptive.binding.AdaptivePropertyProvider
import hu.simplexion.z2.adaptive.binding.AdaptiveStateVariableBinding
import hu.simplexion.z2.adaptive.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestProvider : AdaptivePropertyProvider {

    var i = 12
        set(v) {
            field = v
            bindings.forEach { it.setValue(v, false) }
        }

    val bindings = mutableListOf<AdaptiveStateVariableBinding<*>>()

    override fun addBinding(binding: AdaptiveStateVariableBinding<*>) {
        bindings += binding
    }

    override fun removeBinding(binding: AdaptiveStateVariableBinding<*>) {
        bindings += binding
    }

    @Suppress("RedundantNullableReturnType")
    override fun getValue(path: Array<String>): Any? {
        return i
    }

    override fun setValue(path: Array<String>, value: Any?) {
        i = value as Int
    }

    override fun toString(): String {
        return "TestProvider()"
    }
}

@Suppress("unused")
fun Adaptive.propertyAccessTest() {
    val p = TestProvider()
    propertyAccessor { p.i }
}

lateinit var testBinding : AdaptiveStateVariableBinding<Int>

fun <T> Adaptive.propertyAccessor(
    binding: AdaptiveStateVariableBinding<T>? = null,
    @Suppress("UNUSED_PARAMETER") selector: () -> T
) {
    checkNotNull(binding)
    checkNotNull(binding.propertyProvider)

    @Suppress("UNCHECKED_CAST")
    testBinding = binding as AdaptiveStateVariableBinding<Int>

    T1(binding.value as Int)
}

class PropertyAccessTest {

    @Test
    fun test() {
        val adapter = AdaptiveTestAdapter()
        val root = AdaptiveTestBridge(1)

        AdaptivePropertyAccessBindingTest(adapter, null, 0).apply {
            create()
            mount(root)
        }

        testBinding.setValue(23, true)

        assertEquals(
            adapter.expected(
                listOf(
                    //@formatter:off

                    //@formatter:on
                )
            ),
            adapter.actual(dumpCode = false)
        )
    }
}

class AdaptivePropertyAccessBindingTest(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

    override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> AdaptivePropertyAccessor(adapter, parent, declarationIndex)
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
                        AdaptiveStateVariableBinding<Int>(
                            sourceFragment = this,
                            indexInSourceState = 0,
                            indexInSourceClosure = 0,
                            targetFragment = fragment,
                            indexInTargetState = 0,
                            path = arrayOf("i"),
                            metadata = AdaptivePropertyMetadata("kotlin.Int"),
                            supportFunction = - 1
                        )
                    )
                }
            }
        }
    }

    override fun genPatchInternal() {
        state[0] = TestProvider()
    }

}

class AdaptivePropertyAccessor(
    adapter: AdaptiveAdapter<TestNode>,
    parent: AdaptiveFragment<TestNode>?,
    index: Int
) : AdaptiveFragment<TestNode>(adapter, parent, index, 1) {

    val dependencyMask_0_0 = 0x01 // fragment index: 0, state variable index: 0

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
                    @Suppress("UNCHECKED_CAST")
                    fragment.setStateVariable(0, (getThisClosureVariable(0) as AdaptiveStateVariableBinding<Int>).value)
                }
            }
        }
    }

    override fun genPatchInternal() {
        @Suppress("UNCHECKED_CAST")
        testBinding = state[0] as AdaptiveStateVariableBinding<Int>
    }

}