package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmCall
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmCall2Air(
    parent: ClassBoundIrBuilder,
    val armCall: ArmCall
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.buildBranches += AirBuildBranch(armCall.index, irConstructorCallFromBuild(armCall.target))
        airClass.patchBranches += AirPatchBranch(armCall.index) { irPatchExternalStateVariables(it) }
        invokeBranchExpressions()
    }

    fun irPatchExternalStateVariables(closureMask : IrVariable): IrBlock {
        val function = airClass.patchDescendant
        val fragmentParameter = function.valueParameters.first()

        return IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->
                armCall.arguments.forEach {
                    block.statements += it.toPatchExpression(this, armCall.closure, fragmentParameter, closureMask)
                }
            }
    }

    fun invokeBranchExpressions() {

    }

}
