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
import hu.simplexion.z2.adaptive.testing.T1

@Adaptive
fun Basic(i: Int) {
    val i2 = 12
    T1(0)
//    T1(i)
//    T1(i2)
//    T1(i + i2)
//    if (i == 1) {
//        T1(i2)
//    }
//    when {
//        i == 1 -> T1(i2 + 1)
//        i == 2 -> T1(i2 + 2)
//    }
//    for (fi in i..i2) {
//        T1(fi + i2)
//    }
}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        Basic(11)
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("AdaptiveT1", "init"),
        TraceEvent("AdaptiveT1", "create", "p0:", "0"),
        TraceEvent("AdaptiveT1", "mount", "bridge:", "1")
    ))
}