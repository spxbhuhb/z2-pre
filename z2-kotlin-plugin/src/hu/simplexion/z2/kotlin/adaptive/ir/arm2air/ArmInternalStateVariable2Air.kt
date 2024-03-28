package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmInternalStateVariable

class ArmInternalStateVariable2Air(
    parent: ClassBoundIrBuilder,
    val stateVariable: ArmInternalStateVariable
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirStateVariable =
        with(stateVariable) {
            AirStateVariable(
                indexInState,
                indexInClosure,
                name,
                irVariable.type
            )
        }

}