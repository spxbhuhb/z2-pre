package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchDescendantBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSequence
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name

class ArmSequence2Air(
    parent: ClassBoundIrBuilder,
    val armSequence: ArmSequence
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.buildBranches += AirBuildBranch(armSequence.index, irConstructorCallFromBuild(armSequence.target))
        airClass.patchDescendantBranches += AirPatchDescendantBranch(armSequence.index) { irPatchItemIndices() }
    }

    private fun irPatchItemIndices(): IrExpression = // FIXME do not patch sequence when unnecessary
        irSetDescendantStateVariable(
            Indices.ADAPTIVE_SEQUENCE_ITEM_INDICES,
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.intArray.defaultType,
                irContext.irBuiltIns.findFunctions(Name.identifier("intArrayOf")).single(),
                0, 1,
            ).apply {
                putValueArgument(0,
                    IrVarargImpl(
                        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                        irBuiltIns.intArray.defaultType,
                        irBuiltIns.intType
                    ).apply {
                        elements += armSequence.statements.map { irConst(it.index) }
                    }
                )
            }

        )

}