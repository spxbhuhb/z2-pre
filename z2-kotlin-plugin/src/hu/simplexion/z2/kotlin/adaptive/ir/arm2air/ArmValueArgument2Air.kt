package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmValueArgument
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl

class ArmValueArgument2Air(
    parent: ClassBoundIrBuilder,
    val stateVariable: ArmValueArgument,
    fragmentParameter: IrValueParameter,
    block: IrBlockImpl
) : ClassBoundIrBuilder(parent) {

    fun toPatchExpression() {
        // ircall
    }


}