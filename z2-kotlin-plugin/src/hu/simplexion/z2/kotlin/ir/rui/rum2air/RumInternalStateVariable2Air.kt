package hu.simplexion.z2.kotlin.ir.rui.rum2air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.air.AirStateVariable
import hu.simplexion.z2.kotlin.ir.rui.rum.RumInternalStateVariable

class RumInternalStateVariable2Air(
    parent: ClassBoundIrBuilder,
    val stateVariable: RumInternalStateVariable
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirStateVariable = with(stateVariable) {

        val property = addIrProperty(
            name,
            irVariable.type,
            inIsVar = true,
            inInitializer = irVariable.initializer
        )

        return AirStateVariable(
            this,
            property
        )
    }

}