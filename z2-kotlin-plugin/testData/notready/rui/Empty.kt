/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Rui
import hu.simplexion.z2.adaptive.rui
import hu.simplexion.z2.adaptive.RuiAdapterRegistry
import hu.simplexion.z2.adaptive.testing.RuiTestAdapter
import hu.simplexion.z2.adaptive.testing.RuiTestAdapterFactory

@Rui
fun Empty() {

}

fun box() : String {

    RuiAdapterRegistry.register(RuiTestAdapterFactory)

    rui {
        Empty()
    }

    println(RuiTestAdapter.lastTrace.joinToString("\n"))

    return "OK"
}