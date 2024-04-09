/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

class AdaptiveAccessBinding<T>(
    val owner: AdaptiveFragment<*>,
    val indexInState: Int,
    val indexInClosure: Int,
    val metadata: AdaptivePropertyMetadata
) {
    @Suppress("UNCHECKED_CAST")
    var value: T
        get() = owner.getThisClosureVariable(indexInClosure) as T
        set(v) {
            owner.setStateVariable(indexInState, v)
            owner.patchInternal()
        }

    override fun toString(): String {
        return "AdaptiveAccessBinding(owner=$owner, indexInState=$indexInState, indexInClosure=$indexInClosure type=${metadata.type})"
    }
}