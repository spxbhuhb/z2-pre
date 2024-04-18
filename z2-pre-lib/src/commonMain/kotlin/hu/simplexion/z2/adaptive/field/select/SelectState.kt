package hu.simplexion.z2.adaptive.field.select

import hu.simplexion.z2.schematic.Schematic

class SelectState<T> : Schematic<SelectState<T>>() {

    /**
     * The options the user may choose from. It might be empty if the select is
     * filter based and asynchronous.
     */
    var options by list<T>()

    /**
     * True when there is a query running.
     */
    var running by boolean()

    /**
     * True when the last query returned with no items.
     */
    val noItems
        get() = options.isEmpty()

}