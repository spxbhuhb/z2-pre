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
fun HigherOrder(ho1: Int, @Adaptive func: (pc: Int) -> Unit) {
    func(ho1 * 3)
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        HigherOrder(1) { p1 ->         // local scope
            HigherOrder(p1) { p2 ->     // end scope
                T1(p1 + p2)
            }
        }
    }

    return AdaptiveTestAdapter.assert(
        listOf(
        )
    )
}