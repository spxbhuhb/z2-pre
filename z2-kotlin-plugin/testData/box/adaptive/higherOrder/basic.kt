/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.higherOrderTest() {
    higherFun(12) { lowerFunI1 ->
        higherFun(lowerFunI1) { lowerFunI2 ->
            T1(lowerFunI1 + lowerFunI2)
        }
    }
}

fun Adaptive.higherFun(higherI: Int, lowerFun: Adaptive.(lowerFunI: Int) -> Unit) {
    higherFunInner(higherI * 2) { lowerFunInnerI ->
        lowerFun(higherI + lowerFunInnerI)
    }
}

fun Adaptive.higherFunInner(innerI: Int, lowerFunInner: Adaptive.(lowerFunInnerI: Int) -> Unit) {
    lowerFunInner(innerI + 1)
}

fun box(): String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        higherOrderTest()
    }

    return AdaptiveTestAdapter.assert(
        listOf(
        )
    )
}