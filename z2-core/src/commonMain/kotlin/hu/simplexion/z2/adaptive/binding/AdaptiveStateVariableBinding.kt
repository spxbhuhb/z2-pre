/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.binding

import hu.simplexion.z2.adaptive.AdaptiveFragment

class AdaptiveStateVariableBinding<VT>(
    val sourceFragment: AdaptiveFragment<*>,
    val indexInSourceState: Int,
    val indexInSourceClosure: Int,
    val targetFragment: AdaptiveFragment<*>,
    val indexInTargetState: Int,
    val path: Array<String>?,
    val supportFunction: Int,
    val metadata: AdaptivePropertyMetadata,
) {

    @Suppress("UNCHECKED_CAST")
    val value: VT
        get() {
            val stateValue = sourceFragment.getThisClosureVariable(indexInSourceClosure)
            if (path == null) {
                return stateValue as VT
            } else {
                check(stateValue is AdaptivePropertyProvider)
                return stateValue.getValue(path) as VT
            }
        }

    fun setValue(value: Any?, setProviderValue : Boolean) {
        if (path == null) {
            sourceFragment.setStateVariable(indexInSourceState, value, this)
        } else {
            val provider = sourceFragment.getThisClosureVariable(indexInSourceClosure)

            check(provider is AdaptivePropertyProvider)
            if (setProviderValue) {
                provider.setValue(path, value)
            }

            targetFragment.setDirty(indexInTargetState, true)
        }
    }

    val propertyProvider: Any?
        get() {
            check(path != null) { "this binding does not have a property provider" }
            return sourceFragment.getThisClosureVariable(indexInSourceClosure)
        }

    override fun toString(): String {
        return "AdaptiveStateVariableBinding(${sourceFragment.id}, $indexInSourceState, $indexInSourceState, ${targetFragment.id}, ${indexInTargetState}, ${path.contentToString()}, $supportFunction, $metadata)"
    }

}