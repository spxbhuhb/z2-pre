/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapterFactory
import hu.simplexion.z2.adaptive.testing.AdaptiveTestAdapter.TraceEvent

@Adaptive
fun OnlyExternal(i: Int, s: String) {

}

fun box() : String {

    AdaptiveAdapterRegistry.register(AdaptiveTestAdapterFactory)

    adaptive {
        OnlyExternal(123, "abc")
    }

    return AdaptiveTestAdapter.assert(listOf(
        TraceEvent("<root>", "init", ),
        TraceEvent("AdaptiveOnlyExternal", "init", )
    ))
}