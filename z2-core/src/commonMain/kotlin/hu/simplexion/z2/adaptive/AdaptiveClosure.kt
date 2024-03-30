/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

/**
 * @property  closureSize  The total number of state variables in this closure. This is the sum of the number
 *                         of state variables in [owner] and all [components].
 */
class AdaptiveClosure<BT>(
    val components: Array<AdaptiveFragment<BT>>,
    val closureSize: Int
) {
    val owner
        get() = components[0]

    var declarationScopeSize = owner.state.size

    /**
     * Finds the first parent with `thisClosure` owned by [declaringComponent]. Then extends that closure with
     * the component and returns with the extended closure.
     *
     * Anonymous components use this function to find their declaring closure and extend it with themselves.
     */
    fun extendWith(component: AdaptiveFragment<BT>, declaringComponent: AdaptiveFragment<BT>): AdaptiveClosure<BT> {
        var ancestor = component.parent

        while (ancestor != null && ancestor.thisClosure.owner !== declaringComponent) {
            ancestor = ancestor.parent
        }

        checkNotNull(ancestor) { "couldn't find declaring component for closure extension" }

        val declaringClosure = ancestor.thisClosure

        return AdaptiveClosure(
            declaringClosure.components + component,
            declaringClosure.closureSize + component.state.size
        )
    }

    /**
     * Get a state variable by its index in the closure. Walks over the scopes in the
     * closure to find the state and then fetches the variable form that state.
     */
    fun get(stateVariableIndex: Int): Any? {
        if (stateVariableIndex < declarationScopeSize) {
            return owner.state[stateVariableIndex]
        }

        // indices : 0 1 / 2 / 3 4 5 6 (declaration, anonymous 1, ANONYMOUS-2)
        // requested index: 4
        // closure size of ANONYMOUS-2: 7
        // state size of ANONYMOUS-2: 4
        // index of the first variable of ANONYMOUS-2 in the closure: 3 = closure size - state size
        // index of the requested variable of ANONYMOUS-2 in the state of ANONYMOUS-2: requested index - ANONYMOUS-2 index

        for (anonymousScope in components) {
            val extendedClosureSize = anonymousScope.thisClosure.closureSize
            if (extendedClosureSize > stateVariableIndex) {
                return anonymousScope.state[stateVariableIndex - (extendedClosureSize - anonymousScope.state.size)]
            }
        }

        throw IndexOutOfBoundsException("Invalid state variable index: $stateVariableIndex")
    }

    /**
     * Set a state variable by its index in the closure. Walks over the scopes in the
     * closure to find the state and then sets the variable of that state.
     */
    fun set(stateVariableIndex: Int, value: Any?) {
        if (stateVariableIndex < declarationScopeSize) {
            owner.setStateVariable(stateVariableIndex, value)
            return
        }

        for (anonymousScope in components) {
            val extendedClosureSize = anonymousScope.thisClosure.closureSize
            if (extendedClosureSize > stateVariableIndex) {
                anonymousScope.setStateVariable(stateVariableIndex - (extendedClosureSize - anonymousScope.state.size), value)
                return
            }
        }

        throw IndexOutOfBoundsException("Invalid state variable index: $stateVariableIndex")
    }

    /**
     * Get a state variable from the last component in [components].
     */
    fun getFromLast(variableIndex: Int): Any? =
        components.last().state[variableIndex]

    /**
     * Calculate the complete closure mask (or of components masks).
     */
    fun closureDirtyMask(): AdaptiveStateVariableMask {
        var mask = 0
        var position = 0
        for (component in components) {
            mask = mask or (component.dirtyMask shl position)
            position += component.state.size
        }
        return mask
    }

}