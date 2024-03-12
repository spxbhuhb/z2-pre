package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirFragmentFactory
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

/**
 * External patch generation for sequences. This is just an empty function as sequences do not have
 * state, therefore there is nothing to patch. Sequences call the external patch of the fragments
 * they contain from `patch`.
 */
class AirFragmentFactory2Ir(
    parent: ClassBoundIrBuilder,
    val fragmentFactory: AirFragmentFactory
) : ClassBoundIrBuilder(parent) {

    fun toIr() {
        val irFunction = fragmentFactory.irFunction

        irFunction.body = DeclarationIrBuilder(irContext, irFunction.symbol).irBlockBody {
            val declaringComponent = irFunction.dispatchReceiverParameter !! // the class this builder is member of
            val parent = irTemporary(irGet(irFunction.valueParameters[Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_PARENT]))
            val requestedIndex = irTemporary(irGet(irFunction.valueParameters[Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_INDEX]))

            + irReturn(
                IrWhenImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    classBoundNullableFragmentType,
                    IrStatementOrigin.WHEN
                ).apply {
                    branches += irPlaceholderBranch(requestedIndex) // -1 returns with null

                    fragmentFactory.subBuilders.forEachIndexed { branchIndex, subBuilder ->
                        branches += irFragmentBranch(subBuilder, declaringComponent, parent, requestedIndex, branchIndex)
                    }

                    branches += irElseBranch(requestedIndex)
                }
            )
        }
    }

    fun irPlaceholderBranch(requestedIndex: IrVariable) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(irGet(requestedIndex), irConst(- 1)),
            irNull()
        )

    fun irFragmentBranch(subBuilder: AirBuilder, declaringComponent: IrValueParameter, parent: IrVariable, requestedIndex: IrVariable, branchIndex: Int) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(irGet(requestedIndex), irConst(branchIndex)),
            irCall(
                subBuilder.irFunction.symbol,
                dispatchReceiver = irGet(declaringComponent),
                args = arrayOf(irGet(parent))
            )
        )

    fun IrBlockBodyBuilder.irElseBranch(requestedIndex: IrVariable) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            irCall(
                pluginContext.illegalArgumentExceptionSymbol,
            ).apply {
                putValueArgument(0, irConcat().apply {
                    arguments += irString("unknown branch index: ")
                    arguments += irGet(requestedIndex)
                })
            }
        )

}
