package hu.simplexion.z2.adaptive

/**
 * @property  closureSize  The total number of state variables in this closure. This is the sum of the number
 *                         of state variables in [declarationScope] and all [anonymousScopes].
 */
class AdaptiveClosure<BT>(
    val declarationScope: AdaptiveFragment<BT>,
    val anonymousScopes: Array<AdaptiveAnonymous<BT>>,
    val closureSize: Int
) {
    var adaptiveDirty0 = AdaptiveStateVariableMask(closureSize)
    var declarationScopeSize = declarationScope.state.size

    fun extendWith(scope: AdaptiveAnonymous<BT>): AdaptiveClosure<BT> {
        return AdaptiveClosure(declarationScope, anonymousScopes + scope, closureSize + scope.state.size)
    }

    fun clear() = adaptiveDirty0.clear()

    fun isClearOf(mask: AdaptiveStateVariableMask) = adaptiveDirty0.isClearOf(mask)

    fun isDirtyOf(mask: AdaptiveStateVariableMask) = adaptiveDirty0.isDirtyOf(mask)

    fun copyFrom(other: AdaptiveClosure<BT>) {
        adaptiveDirty0.copyFrom(other.adaptiveDirty0)
    }

    fun invalidate(stateVariableIndex: Int) {
        adaptiveDirty0.invalidate(stateVariableIndex)
    }

    /**
     * Get a state variable by its index in the closure. Walks over the scopes in the
     * closure to find the state and then fetches the variable form that state.
     */
    fun get(stateVariableIndex: Int): Any? {
        if (stateVariableIndex < declarationScopeSize) {
            return declarationScope.state[stateVariableIndex]
        }

        // indices : 0 1 / 2 / 3 4 5 6 (declaration, anonymous 1, ANONYMOUS-2)
        // requested index: 4
        // closure size of ANONYMOUS-2: 7
        // state size of ANONYMOUS-2: 4
        // index of the first variable of ANONYMOUS-2 in the closure: 3 = closure size - state size
        // index of the requested variable of ANONYMOUS-2 in the state of ANONYMOUS-2: requested index - ANONYMOUS-2 index

        for (anonymousScope in anonymousScopes) {
            val extendedClosureSize = anonymousScope.extendedClosure.closureSize
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
            declarationScope.state[stateVariableIndex] = value
        } else {
            for (anonymousScope in anonymousScopes) {
                val extendedClosureSize = anonymousScope.extendedClosure.closureSize
                if (extendedClosureSize > stateVariableIndex) {
                    anonymousScope.state[stateVariableIndex - (extendedClosureSize - anonymousScope.state.size)] = value
                }
            }
        }

        invalidate(stateVariableIndex)

        throw IndexOutOfBoundsException("Invalid state variable index: $stateVariableIndex")
    }

    fun getBoolean(stateVariableIndex: Int) : Boolean = get(stateVariableIndex) as Boolean
    fun setBoolean(stateVariableIndex: Int, value : Boolean) = set(stateVariableIndex, value)

    fun getInt(stateVariableIndex: Int) : Int = get(stateVariableIndex) as Int
    fun setInt(stateVariableIndex: Int, value : Int) = set(stateVariableIndex, value)

    fun getLong(stateVariableIndex: Int) : Long = get(stateVariableIndex) as Long
    fun setLong(stateVariableIndex: Int, value : Long) = set(stateVariableIndex, value)

    fun getFloat(stateVariableIndex: Int) : Float = get(stateVariableIndex) as Float
    fun setFloat(stateVariableIndex: Int, value : Float) = set(stateVariableIndex, value)

    fun getDouble(stateVariableIndex: Int) : Double = get(stateVariableIndex) as Double
    fun setDouble(stateVariableIndex: Int, value : Double) = set(stateVariableIndex, value)

    fun getString(stateVariableIndex: Int) : String = get(stateVariableIndex) as String
    fun setString(stateVariableIndex: Int, value : Int) = set(stateVariableIndex, value)

}