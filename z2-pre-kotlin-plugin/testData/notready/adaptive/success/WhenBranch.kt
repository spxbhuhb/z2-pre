/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter.TraceEvent
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapterFactory
import hu.simplexion.z2.adaptive.testing.T0
import hu.simplexion.z2.adaptive.testing.T1

@Adaptive
fun WhenBranch(i: Int) {
    when {
        i == 0 -> T0
        i == 1 -> T1(-1)
        else -> T1(i)
    }
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        WhenBranch(1)
    }

    return AdaptiveTestAdapter.assert(
        listOf(
        )
    )
}