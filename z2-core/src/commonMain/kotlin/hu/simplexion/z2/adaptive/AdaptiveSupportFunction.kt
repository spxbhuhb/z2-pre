/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveSupportFunction<BT>(
    val fragment: AdaptiveFragment<BT>,
    val supportFunctionIndex : Int
) {

    fun invoke(vararg arguments : Any?) : Any? {
        return fragment.invoke(this, arguments)
    }

    override fun toString() =
        "AdaptiveSupportFunction(${fragment.id}, $supportFunctionIndex)"
}