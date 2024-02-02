package hu.simplexion.z2.rui

/**
 * @property  closureSize  The total number of state variables in this closure. This is the sum of the number
 *                         of state variables in [declarationScope] and all [anonymousScopes].
 */
class RuiClosure<BT>(
    val declarationScope: RuiFragment<BT>,
    val anonymousScopes: Array<RuiAnonymous<BT>>,
    val closureSize : Int
) {
    var ruiDirty0 = RuiStateVariableMask(closureSize)

    fun extendWith(scope: RuiAnonymous<BT>): RuiClosure<BT> {
        return RuiClosure(declarationScope, anonymousScopes + scope, closureSize + scope.ruiState.size)
    }

    fun clear() = ruiDirty0.clear()

    fun isClearOf(mask: RuiStateVariableMask) = ruiDirty0.isClearOf(mask)

    fun isDirtyOf(mask: RuiStateVariableMask) = ruiDirty0.isDirtyOf(mask)

    fun copyFrom(other : RuiClosure<BT>) {
        ruiDirty0.copyFrom(other.ruiDirty0)
    }

    fun invalidate(stateVariableIndex: Int) {
        ruiDirty0.invalidate(stateVariableIndex)
    }
}