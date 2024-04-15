/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.*
import hu.simplexion.z2.adaptive.testing.*

var actual : TestState? = null

class TestState(
    override val fragment : AdaptiveFragment<*>
) : AdaptiveStateApi {
    override val state
        get() = fragment.state
}

fun box() : String {

    val testAdapter = AdaptiveTestAdapter()

    adaptive(testAdapter) {
        actual = thisState()
    }

    val expected = testAdapter.rootFragment.state

    return if (expected === actual?.state) "OK" else "Fail: returned fragment is not the same: $expected $actual"
    
}