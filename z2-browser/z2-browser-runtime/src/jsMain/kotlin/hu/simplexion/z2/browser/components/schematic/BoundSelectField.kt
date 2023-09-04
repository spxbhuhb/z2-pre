package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.html.Z2

interface BoundSelectField<T> : BoundField<T> {

    var itemBuilderFun : Z2.(value : T) -> Unit
    var queryFun : (suspend () -> List<T>)?
    var options : List<T>

    infix fun itemBuilder(itemBuilderFun : Z2.(value : T) -> Unit) : BoundSelectField<T> {
        this.itemBuilderFun = itemBuilderFun
        return this
    }

    infix fun query(queryFun : suspend () -> List<T>) : BoundSelectField<T> {
        this.queryFun = queryFun
        return this
    }

    infix fun options(options : List<T>) : BoundSelectField<T> {
        this.options = options
        return this
    }


    infix fun options(options : () -> List<T>) : BoundSelectField<T> {
        this.options = options()
        return this
    }

}