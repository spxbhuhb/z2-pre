/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

fun Adaptive.Basic(i : Int, supportFun : (i : Int) -> Unit) {
    supportFun(i)
}

var a = 0

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        Basic(12) { a = it }
    }

    if (a != 12) return "Fail: a != 12"

    return AdaptiveTestAdapter.assert(listOf(

    ))
}