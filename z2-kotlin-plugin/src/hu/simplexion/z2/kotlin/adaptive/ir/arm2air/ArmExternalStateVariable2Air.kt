package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmExternalStateVariable

class ArmExternalStateVariable2Air(
    parent: ClassBoundIrBuilder,
    val stateVariable: ArmExternalStateVariable
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