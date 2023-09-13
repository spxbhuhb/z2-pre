package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.radiobutton.RadioButtonGroup
import hu.simplexion.z2.schematic.runtime.access.SchematicAccessContext

class BoundSelectField<T>(
    parent: Z2,
    context: SchematicAccessContext,
    buildFun: BoundSelectField<T>.() -> ValueField<T>
) : BoundField<T>(
    parent, context, { buildFun(this as BoundSelectField<T>) } // TODO clean buildFuns of bound fields
) {

    infix fun itemBuilder(itemBuilderFun: Z2.(value: T) -> Unit): BoundSelectField<T> {
        (uiField as RadioButtonGroup<T>).config.itemBuilderFun = itemBuilderFun
        return this
    }

    infix fun options(options: List<T>): BoundSelectField<T> {
        (uiField as RadioButtonGroup<T>).config.options = options
        return this
    }

    infix fun options(options: () -> List<T>): BoundSelectField<T> {
        (uiField as RadioButtonGroup<T>).config.options = options()
        return this
    }

}