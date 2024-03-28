package hu.simplexion.z2.kotlin.adaptive.ir.arm2air

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuildBranch
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirPatchBranch
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmCall
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmCall2Air(
    parent: ClassBoundIrBuilder,
    val armCall: ArmCall
) : ClassBoundIrBuilder(parent) {

    fun toAir() {
        airClass.buildBranches += AirBuildBranch(armCall.index, irBuildExpression())
        airClass.patchBranches += AirPatchBranch(armCall.index, irPatchExpression())
        invokeBranches()
    }

    fun irBuildExpression(): IrExpression {
        val symbolMap = armCall.symbolMap(this)
        val buildFun = airClass.build

        val constructorCall =
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                symbolMap.defaultType,
                symbolMap.primaryConstructor.symbol,
                typeArgumentsCount = 1, // bridge type
                constructorTypeArgumentsCount = 0,
                Indices.ADAPTIVE_FRAGMENT_ARGUMENT_COUNT
            )

        constructorCall.putTypeArgument(Indices.ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE, classBoundBridgeType.defaultType)

        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGetValue(airClass.adapter, irGet(buildFun.dispatchReceiverParameter !!)))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irGet(buildFun.valueParameters[Indices.ADAPTIVE_BUILDER_PARENT]))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irGet(buildFun.valueParameters[Indices.ADAPTIVE_BUILDER_DECLARATION_INDEX]))

        return constructorCall
    }

    fun irPatchExpression(): IrBlock {
        val function = airClass.patch
        val fragmentParameter = function.valueParameters.first()

        return IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->
                armCall.arguments.forEach {
                    it.toPatchExpression(this, fragmentParameter, block)
                }
            }
    }

    fun invokeBranches() {
        TODO()
    }

}
