package hu.simplexion.z2.adaptive

/**
 * @property  closureSize  The total number of state variables in this closure. This is the sum of the number
 *                         of state variables in [declarationScope] and all [anonymousScopes].
 */
class AdaptiveClosure<BT>(
    val declarationScope: AdaptiveFragment<BT>,
    val anonymousScopes: Array<AdaptiveAnonymous<BT>>,
    val closureSize : Int
) {
    var adaptiveDirty0 = AdaptiveStateVariableMask(closureSize)

    fun extendWith(scope: AdaptiveAnonymous<BT>): AdaptiveClosure<BT> {
        return AdaptiveClosure(declarationScope, anonymousScopes + scope, closureSize + scope.adaptiveState.size)
    }

    fun clear() = adaptiveDirty0.clear()

    fun isClearOf(mask: AdaptiveStateVariableMask) = adaptiveDirty0.isClearOf(mask)

    fun isDirtyOf(mask: AdaptiveStateVariableMask) = adaptiveDirty0.isDirtyOf(mask)

    fun copyFrom(other : AdaptiveClosure<BT>) {
        adaptiveDirty0.copyFrom(other.adaptiveDirty0)
    }

    fun invalidate(stateVariableIndex: Int) {
        adaptiveDirty0.invalidate(stateVariableIndex)
    }
}