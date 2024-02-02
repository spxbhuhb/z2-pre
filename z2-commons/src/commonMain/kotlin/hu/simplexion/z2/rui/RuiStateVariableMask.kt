package hu.simplexion.z2.rui

class RuiStateVariableMask private constructor(
    private val size: Int,
    private var value: Int
) {

    companion object {
        fun of(vararg dependencies: Int) =
            RuiStateVariableMask(dependencies.reduce { acc, i -> (1 shl i) or acc }, dependencies.size)

        fun of(vararg dependencies: RuiStateVariableMask) =
            RuiStateVariableMask(
                dependencies.map { it.value }.reduce { acc, i -> acc or i },
                dependencies.maxOfOrNull { it.size } ?: 0
            )

    }

    constructor(size: Int) : this(size, 0)

    fun clear() {
        value = 0
    }

    fun copyFrom(other: RuiStateVariableMask) {
        value = other.value
    }

    fun invalidate(stateVariableIndex: Int) {
        value = value or (1 shl stateVariableIndex)
    }

    fun isClear() =
        (value == 0)

    fun isClearOf(other: RuiStateVariableMask) =
        ((value and other.value) == 0)

    fun isDirtyOf(other: RuiStateVariableMask) =
        ((value and other.value) == 0)

    /**
     * True when the [stateVariableIndex]th bit is 1 in the `this`, false otherwise.
     *
     * This check is **NOT** closure aware. Its intended use is from the external
     * patch function of `RuiAnonymous` components.
     */
    fun isClearOf(stateVariableIndex: Int) =
        ((value and stateVariableIndex) == 0)

    /**
     * True when the [stateVariableIndex]th bit is 0 in the `this`, false otherwise.
     *
     * This check is **NOT** closure aware. Its intended use is from the external
     * patch function of `RuiAnonymous` components.
     */
    fun isDirtyOf(stateVariableIndex: Int) =
        ((value and stateVariableIndex) != 0)

}