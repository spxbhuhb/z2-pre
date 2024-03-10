package hu.simplexion.z2.kotlin.adaptive.ir.rum2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.rum.RumExternalStateVariable

class RumExternalStateVariable2Air(
    parent: ClassBoundIrBuilder,
    val stateVariable: RumExternalStateVariable
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirStateVariable = with(stateVariable) {

        val property = addPropertyWithConstructorParameter(
            name,
            irValueParameter.type,
            inIsVar = true,
            inVarargElementType = irValueParameter.varargElementType
        )

        return AirStateVariable(
            this,
            property
        )
    }


}