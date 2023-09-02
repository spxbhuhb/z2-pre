package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField

interface BoundField<T> {
    val schematic : Schematic<*>
    val field : SchemaField<T>

    var fullSuspendValidation : FullSuspendValidation<T>?

    infix fun validateSuspend(validation : SuspendValidation<T>)  {
        fullSuspendValidation = { _,_,value -> validation(value) }
    }

    infix fun validateSuspendFull(validation : FullSuspendValidation<T>) {
        fullSuspendValidation = validation
    }

}