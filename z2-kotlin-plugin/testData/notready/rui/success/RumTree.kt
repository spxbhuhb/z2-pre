/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Rui
import hu.simplexion.z2.adaptive.rui
import hu.simplexion.z2.adaptive.RuiAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

@Rui
fun Call() {
    T0()
}

@Rui
fun Sequence() {
    T0()
    T0()
}

@Rui
fun When(i : Int) {
    if (i % 2 == 1) {
        T1(1)
    } else {
        T1(2)
    }
}

@Rui
fun ForLoop(ei : Int) {
    for (i in 1..10) {
        T1(i)
        T1(ei+i)
    }
}

@Rui
fun HigherOrder(i : Int) {
    H2(1) { p1 ->
        H1 {
            T1(i + p1)
        }
    }
}


/**
 * This test is to display the structures built by the plugin. It does not perform actual
 * checks on the result.
 */
fun box() : String {
    return "OK"
}