package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext

class BoundSelectField<T>(
    parent: Z2,
    context: SchematicAccessContext,
    buildFun: BoundSelectField<T>.() -> ValueField<T>
) : BoundField<T>(
    parent, context, { buildFun(this as BoundSelectField<T>) } // TODO clean buildFuns of vound fields
) {

    var itemBuilderFun: Z2.(value: T) -> Unit = { + it.toString() }
    var queryFun: (suspend () -> List<T>)? = { emptyList() }
    var options: List<T> = emptyList()

    infix fun itemBuilder(itemBuilderFun: Z2.(value: T) -> Unit): BoundSelectField<T> {
        this.itemBuilderFun = itemBuilderFun
        return this
    }

    infix fun query(queryFun: suspend () -> List<T>): BoundSelectField<T> {
        this.queryFun = queryFun
        return this
    }

    infix fun options(options: List<T>): BoundSelectField<T> {
        this.options = options
        return this
    }

    infix fun options(options: () -> List<T>): BoundSelectField<T> {
        this.options = options()
        return this
    }

}