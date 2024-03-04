package hu.simplexion.z2.adaptive.field

import hu.simplexion.z2.schematic.Schematic

class FieldValue<T> : Schematic<FieldValue<T>>() {

    /**
     * The current value of the field. If there is no value (for example a select
     * with no options selected yet) getting [value] throws a [IllegalStateException].
     */
    var value
        get() = checkNotNull(valueOrNull)
        set(value) {
            valueOrNull = value
        }

    /**
     * The current value of the field or null if there is no value (for example a select
     * with no options selected yet).
     */
    var valueOrNull by generic<T?>()

}