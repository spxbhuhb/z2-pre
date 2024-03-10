/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.success

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.AdaptiveAdapterRegistry
import hu.simplexion.z2.adaptive.testing.*

@Adaptive
fun Call() {
    T0()
}

@Adaptive
fun Sequence() {
    T0()
    T0()
}

@Adaptive
fun When(i : Int) {
    if (i % 2 == 1) {
        T1(1)
    } else {
        T1(2)
    }
}

@Adaptive
fun ForLoop(ei : Int) {
    for (i in 1..10) {
        T1(i)
        T1(ei+i)
    }
}

@Adaptive
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