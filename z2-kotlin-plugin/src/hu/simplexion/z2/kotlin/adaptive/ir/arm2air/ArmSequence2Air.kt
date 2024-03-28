package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSequence
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstantArrayImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstantPrimitiveImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmSequence2Air(
    parent: ClassBoundIrBuilder,
    val armSequence: ArmSequence
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.buildBranches += AirBuildBranch(armSequence.index, irBuildExpression(armSequence.symbolMap(this)))
        airClass.patchBranches += AirPatchBranch(armSequence.index, irPatchExpression())
    }

    fun irPatchExpression(): IrBlock =
        IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->
                block.statements +=
                    IrCallImpl(
                        SYNTHETIC_OFFSET,
                        SYNTHETIC_OFFSET,
                        irBuiltIns.unitType,
                        pluginContext.setStateVariable,
                        typeArgumentsCount = 0,
                        valueArgumentsCount = 1
                    ).also { call ->

                        call.dispatchReceiver = irGet(airClass.patch.valueParameters.first())

                        call.putValueArgument(
                            Indices.ADAPTIVE_SET_STATE_VARIABLE_INDEX,
                            irConst(0)
                        )

                        call.putValueArgument(
                            Indices.ADAPTIVE_SET_STATE_VARIABLE_VALUE,
                            IrConstantArrayImpl(
                                startOffset = SYNTHETIC_OFFSET,
                                endOffset = SYNTHETIC_OFFSET,
                                irBuiltIns.intArray.defaultType,
                                armSequence.statements.map { IrConstantPrimitiveImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irConst(it.index)) }
                            )
                        )

                    }
            }
}