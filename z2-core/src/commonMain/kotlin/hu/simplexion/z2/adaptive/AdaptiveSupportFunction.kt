/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveSupportFunction<BT>(
    fragment: AdaptiveFragment<BT>,
    val index : Int
) {
    val closure = fragment.createClosure!!

    fun invoke(vararg arguments : Any?) : Any? {
        return closure.owner.invoke(this, arguments)
    }
}