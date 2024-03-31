package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmLoop
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmLoop2Air(
    parent: ClassBoundIrBuilder,
    val armLoop: ArmLoop
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.buildBranches += AirBuildBranch(armLoop.index, irConstructorCallFromBuild(armLoop.target))
        airClass.patchBranches += AirPatchBranch(armLoop.index) { irPatchIteratorAndFactory() }
    }

    fun irPatchIteratorAndFactory(): IrExpression {
        val function = airClass.patchDescendant

        return IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->

                 val iteratorInitializer = ((armLoop.iterator.irDeclaration as IrVariable).initializer!!)

                 block.statements += irSetDescendantStateVariable(
                     Indices.ADAPTIVE_LOOP_ITERATOR,
                     iteratorInitializer.transformStateAccess(armLoop.closure) { irGet(function.dispatchReceiverParameter !!) }
                 )

                 block.statements += irSetDescendantStateVariable(
                     Indices.ADAPTIVE_LOOP_FACTORY,
                     irFragmentFactoryFromPatch(armLoop.body.index)
                 )

            }
    }

}