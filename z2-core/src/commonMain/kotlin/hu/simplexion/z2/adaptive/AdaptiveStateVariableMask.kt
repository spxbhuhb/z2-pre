package hu.simplexion.z2.adaptive

class AdaptiveStateVariableMask private constructor(
    private val size: Int,
    private var value: Int
) {

    companion object {
        fun of(vararg dependencies: Int) =
            AdaptiveStateVariableMask(dependencies.reduce { acc, i -> (1 shl i) or acc }, dependencies.size)

        fun of(vararg dependencies: AdaptiveStateVariableMask) =
            AdaptiveStateVariableMask(
                dependencies.map { it.value }.reduce { acc, i -> acc or i },
                dependencies.maxOfOrNull { it.size } ?: 0
            )

    }

    constructor(size: Int) : this(size, 0)

    fun clear() {
        value = 0
    }

    fun copyFrom(other: AdaptiveStateVariableMask) {
        value = other.value
    }

    fun invalidate(stateVariableIndex: Int) {
        value = value or (1 shl stateVariableIndex)
    }

    fun isClear() =
        (value == 0)

    fun isClearOf(other: AdaptiveStateVariableMask) =
        ((value and other.value) == 0)

    fun isDirtyOf(other: AdaptiveStateVariableMask) =
        ((value and other.value) == 0)

    /**
     * True when the [stateVariableIndex]th bit is 1 in the `this`, false otherwise.
     *
     * This check is **NOT** closure aware. Its intended use is from the external
     * patch function of `AdaptiveAnonymous` components.
     */
    fun isClearOf(stateVariableIndex: Int) =
        ((value and stateVariableIndex) == 0)

    /**
     * True when the [stateVariableIndex]th bit is 0 in the `this`, false otherwise.
     *
     * This check is **NOT** closure aware. Its intended use is from the external
     * patch function of `AdaptiveAnonymous` components.
     */
    fun isDirtyOf(stateVariableIndex: Int) =
        ((value and stateVariableIndex) != 0)

    override fun toString() = "(size=$size, value=$value)"

}