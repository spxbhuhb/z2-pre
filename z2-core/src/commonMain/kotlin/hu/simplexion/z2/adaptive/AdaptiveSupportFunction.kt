/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveSupportFunction<BT>(
    val declaringFragment: AdaptiveFragment<BT>,
    val receivingFragment: AdaptiveFragment<BT>,
    val supportFunctionIndex : Int
) {

    /**
     * Invokes the function in the closure of [receivingFragment]. As the function declaration
     * may be somewhere in anonymous components, the function execution may need variables from the
     * closure of those anonymous fragments.
     */
    fun invoke(vararg arguments: Any?): Any? {
        return declaringFragment.invoke(this, arguments)
    }

    suspend fun invokeSuspend(vararg arguments: Any?): Any? {
        return declaringFragment.invokeSuspend(this, arguments)
    }

    override fun toString() =
        "AdaptiveSupportFunction(${declaringFragment.id}, $supportFunctionIndex)"
}