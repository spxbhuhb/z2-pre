package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirExternalPatchCall
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.StateAccessTransform.Companion.transformStateAccess
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmExpression
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.util.defaultType

/**
 * Generates external patch for calls.
 *
 * The generated code looks like this (NNN = call site offset, CCC = child component name):
 *
 * ```kotlin
 *     fun adaptiveEpNNN(it: AdaptiveFragment<BT>, scopeMask: Long): Long { // buildExternalPatch
 *         adaptiveAdapter.trace("<component-name>", "adaptiveEpNNN", "scopeMask", scopeMask, "callSiteDependencies", callSiteDependencies)   // traceExternalPatch
 *         if (scopeMask and callSiteDependency != 0L) return 0L  // irExternalPatchBody
 *
 *         it as AdaptiveCCC // irExternalPatchBody
 *         if (adaptiveDirty0 and 1L != 0L) { // irVariablePatchCondition
 *             it.p0 = i   // irVariableSetAndInvalidate
 *             it.adaptiveInvalidate0(1L) // irVariableSetAndInvalidate
 *         }
 *
 *         return 0L // irExternalPatchBody
 *     }
 * ```
 */
class AirExternalPatchCall2Ir(
    parent: ClassBoundIrBuilder,
    val externalPatch: AirExternalPatchCall
) : ClassBoundIrBuilder(parent) {

    val armCall
        get() = externalPatch.armElement

    val arguments
        get() = armCall.valueArguments

    val dispatchReceiver
        get() = externalPatch.irFunction.dispatchReceiverParameter!!

    val symbolMap = armCall.symbolMap(this)

    val callSiteDependencyMask = calcCallSiteDependencyMask()

    fun toIr() {
        val function = externalPatch.irFunction

        val externalPatchIt = function.valueParameters[Indices.ADAPTIVE_EXTERNAL_PATCH_FRAGMENT]

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {

            +irAs(irGet(externalPatchIt), symbolMap.defaultType)

            arguments.forEachIndexed { index, adaptiveExpression ->
                irVariablePatch(externalPatchIt, index, adaptiveExpression)
            }
        }
    }

    private fun calcCallSiteDependencyMask(): Long {
        var mask = 0L
        for (argument in arguments) {
            for (index in argument.dependencies.map { it.index }) {
                mask = mask or (1L shl index)
            }
        }
        return mask
    }

    private fun IrBlockBodyBuilder.irVariablePatch(externalPatchIt: IrValueDeclaration, index: Int, expression: ArmExpression) {
        // constants, globals, etc. have no dependencies, no need to patch them
        if (expression.dependencies.isEmpty()) return

        // lambdas and anonymous functions cannot change and cannot be patched
        // FIXME check if lambda and anonymous func results in IrFunctionExpression
        if (expression.irExpression is IrFunctionExpression) return

        +irIf(
            irVariablePatchCondition(expression),
            irVariableSetAndInvalidate(externalPatchIt, index, expression)
        )
    }

    private fun irVariablePatchCondition(expression: ArmExpression): IrExpression {

        var result : IrExpression? = null

        for (dependency in expression.dependencies) {
            val (stateVariable, scopePath) = checkNotNull(airClass.findStateVariable(context, dependency.name.identifier)) { "missing state variable: $dependency"}

            result = if (result == null) {
                stateVariable.irIsDirty(scopePath.getScopeFragment(dispatchReceiver))
            } else {
                irOrOr(result, stateVariable.irIsDirty(scopePath.getScopeFragment(dispatchReceiver)))
            }
        }

        return checkNotNull(result)
    }

    fun List<AirClass>.getScopeFragment(dispatchReceiver : IrValueParameter) : IrExpression {
        var result = irImplicitAs(airClass.irClass.defaultType, irGet(dispatchReceiver))

        for (parentScope in this.drop(1)) {
            result = irImplicitAs(parentScope.irClass.defaultType, result)
        }

        return result
    }

    fun AirStateVariable.irIsDirty(receiver: IrExpression): IrExpression {
        val variableIndex = armElement.index
        val maskIndex = variableIndex / ADAPTIVE_STATE_VARIABLE_LIMIT
        val bitIndex = variableIndex % ADAPTIVE_STATE_VARIABLE_LIMIT

        val mask = airClass.dirtyMasks[maskIndex]

        return irNotEqual(
            irAnd(irGetValue(mask.irProperty, receiver), irConst(1L shl bitIndex)),
            irConst(0L)
        )
    }

    private fun IrBlockBodyBuilder.irVariableSetAndInvalidate(externalPatchIt: IrValueDeclaration, index: Int, adaptiveExpression: ArmExpression): IrExpression {
        return irBlock {

            val newValue = irTemporary(
                transformStateAccess(adaptiveExpression, dispatchReceiver.symbol)
            )

            // set the state variable in the child fragment
            +irCall(
                symbolMap.setterFor(index),
                origin = null,
                dispatchReceiver = irImplicitAs(symbolMap.defaultType, irGet(externalPatchIt)),
                extensionReceiver = null,
                irGet(newValue)
            )

            // call invalidate of the child fragment
            +irCall(
                symbolMap.getInvalidate(index / ADAPTIVE_STATE_VARIABLE_LIMIT), null,
                irImplicitAs(symbolMap.defaultType, irGet(externalPatchIt)), null,
                irConst(1L shl (index % ADAPTIVE_STATE_VARIABLE_LIMIT))
            )

        }
    }
}
