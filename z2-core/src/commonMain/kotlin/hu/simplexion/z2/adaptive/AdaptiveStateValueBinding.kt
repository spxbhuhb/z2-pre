/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

import hu.simplexion.z2.meta.PropertyMetadata
import hu.simplexion.z2.meta.ValueBinding

data class AdaptiveStateValueBinding<VT>(
    val owner: AdaptiveFragment<*>,
    val indexInState: Int,
    val indexInClosure: Int,
    val supportFunction: Int,
    override val metadata: PropertyMetadata,
    override val callback: ((binding : ValueBinding<*>)->Unit)? = null
) : ValueBinding<VT> {

    @Suppress("UNCHECKED_CAST")
    override var value: VT
        get() = owner.getThisClosureVariable(indexInClosure) as VT
        set(v) {
            owner.setStateVariable(indexInState, v, this)
            owner.patchInternal()
        }

    override fun replaces(other: ValueBinding<*>) : Boolean {
        // TODO think about value binding replace
        return false
    }

    override fun toString(): String {
        return "AdaptiveStateValueBinding(${owner.id}, $indexInState, $indexInClosure, $metadata, $supportFunction)"
    }
}