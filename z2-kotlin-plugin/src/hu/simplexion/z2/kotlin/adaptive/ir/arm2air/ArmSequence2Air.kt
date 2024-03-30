package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSequence
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstantArrayImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstantPrimitiveImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmSequence2Air(
    parent: ClassBoundIrBuilder,
    val armSequence: ArmSequence
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.buildBranches += AirBuildBranch(armSequence.index, irConstructorCallFromBuild(armSequence.target))
        airClass.patchBranches += AirPatchBranch(armSequence.index, irPatchItemIndices())
    }

    private fun irPatchItemIndices(): IrExpression =
        irSetStateVariable(
            Indices.ADAPTIVE_SEQUENCE_ITEM_INDICES,
            IrConstantArrayImpl(
                startOffset = SYNTHETIC_OFFSET,
                endOffset = SYNTHETIC_OFFSET,
                irBuiltIns.intArray.defaultType,
                armSequence.statements.map { IrConstantPrimitiveImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irConst(it.index)) }
            )
        )

}