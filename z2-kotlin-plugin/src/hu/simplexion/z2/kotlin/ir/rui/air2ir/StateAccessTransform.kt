/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.air2ir

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.RUI_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.ir.rui.air.AirClass
import hu.simplexion.z2.kotlin.ir.rui.air.AirStateVariable
import hu.simplexion.z2.kotlin.ir.rui.diagnostics.ErrorsRui.RUI_IR_RENDERING_VARIABLE
import hu.simplexion.z2.kotlin.ir.rui.diagnostics.ErrorsRui.RUI_IR_STATE_VARIABLE_SHADOW
import hu.simplexion.z2.kotlin.ir.rui.rum.RumExpression
import hu.simplexion.z2.kotlin.ir.rui.util.RuiAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.deepCopyWithVariables
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.interpreter.toIrConst
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.psi.KtModifierListOwner

/**
 * Transforms state variable accesses from the original code into property
 * access in the generated IrClass.
 *
 * # General transform
 *
 * - original function parameter reads (IrGetValue):
 *   - transformed into state variable getter calls (start scope)
 * - top level local variable reads (IrCall to the getter)
 *   - transformed into state variable getter calls (start scope)
 * - top level local variable writes (IrCall to the setter)
 *   - transformed into state variable setter calls (start scope)
 * - anonymous function parameter reads (IrGetValue)
 *    - transformed into state variable getter calls (intermediate or end scope)
 *
 * # Local function transform
 *
 * In addition to the general transform, local functions that modify state variables
 * are extended with dirty mask update and patch call.
 *
 * - `ruiInvalidate0(stateVariableIndex)` calls to invalidate the variable
 * - `ruiPatch(ruiDirty0)` calls to execute patch when necessary
 *
 * @param  startScopeValue  the value that stores the instance of the start scope class
 */
class StateAccessTransform(
    private val irBuilder: ClassBoundIrBuilder,
    private val startScopeValue: IrValueSymbol
) : IrElementTransformerVoidWithContext(), RuiAnnotationBasedExtension {

    companion object {

        /**
         * Transforms variable access in external patch and CALL rendering builder.
         */
        fun ClassBoundIrBuilder.transformStateAccess(expression: RumExpression, startScopeValue: IrValueSymbol): IrExpression =
            expression
                .irExpression
                .deepCopyWithVariables()
                .transform(StateAccessTransform(this, startScopeValue), null)

        /**
         * Transforms an initializer statement.
         */
        fun ClassBoundIrBuilder.transformStateAccess(statement: IrStatement, startScopeValue: IrValueSymbol): IrStatement =
            statement
                .deepCopyWithVariables()
                .transform(StateAccessTransform(this, startScopeValue), null)
                as IrStatement
    }

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        irBuilder.context.annotations

    val airClass: AirClass
        get() = irBuilder.airClass

    val rumClass
        get() = airClass.rumClass

    val irContext = irBuilder.irContext

    val irBuiltIns = irContext.irBuiltIns

    fun scopeReceiver() = irBuilder.irImplicitAs(startScopeValue.owner.type, IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, startScopeValue))

    var haveToPatch = false

    override fun visitVariable(declaration: IrVariable): IrStatement {
        RUI_IR_RENDERING_VARIABLE.check(airClass.rumClass, declaration) {
            declaration.startOffset < airClass.rumClass.boundary.startOffset ||
                declaration.origin == IrDeclarationOrigin.FOR_LOOP_ITERATOR ||
                declaration.origin == IrDeclarationOrigin.FOR_LOOP_VARIABLE ||
                declaration.origin == IrDeclarationOrigin.IR_TEMPORARY_VARIABLE
        }

        return super.visitVariable(declaration)
    }

    /**
     * Transforms non-rendering functions such as callbacks defined inside the original
     * function. When these functions modify state variables they have to call patch
     * before they return.
     */
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        haveToPatch = false
        val transformed = super.visitFunctionNew(declaration) as IrFunction
        if (! haveToPatch) return transformed

        val ps = parentScope // this is the IR scope, not the Rui scope

        return when {
            ps == null -> addPatch(transformed)
            ps.irElement is IrProperty -> TODO()
            ps.irElement is IrClass -> TODO()
            else -> transformed
        }
    }

    /**
     * Adds a call to the `ruiPatch` function as the last statement of the function body to
     * notify the components about the state change.
     */
    fun addPatch(function: IrFunction): IrFunction {
        val body = function.body ?: return function

        function.body = DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            for (statement in body.statements) + statement

            // FIXME this applies patch to the very end of the function, it should cover all returns
            // FIXME analyze all the possible uses of state changing functions and the possible scopes

            // SOURCE  patch(ruiDirty0)
            + irCall(
                airClass.patch.symbol,
                irBuiltIns.unitType,
                valueArgumentsCount = 1,
                typeArgumentsCount = 0,
                origin = IrStatementOrigin.INVOKE
            ).apply {
                dispatchReceiver = scopeReceiver()
            }
        }

        return function
    }

    /**
     * Replace local variable get with scope-aware property get. Traverses up in the scopes
     * to find the one that stores the state variable. The scope that stores the state
     * variable has to be cast to the proper type, or we'll get a compilation error.
     */
    override fun visitGetValue(expression: IrGetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        val (stateVariable,path) = airClass.findStateVariable(irBuilder.context, name) ?: return super.visitGetValue(expression)

        RUI_IR_STATE_VARIABLE_SHADOW.check(rumClass, expression) { stateVariable.rumElement.matches(expression.symbol) }

        return irBuilder.irGetValue(stateVariable.irProperty, scopeReceiver(/* path */))
    }

    /**
     * Replaces local variable change with class property change.
     *
     * Replaces only top level function variables. Others (one defined in a block)
     * are not reactive, and should not be replaced.
     */
    override fun visitSetValue(expression: IrSetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        val stateVariable = airClass.stateVariableMap[name]
            ?: return super.visitSetValue(expression)

        RUI_IR_STATE_VARIABLE_SHADOW.check(rumClass, expression) { stateVariable.rumElement.matches(expression.symbol) }

        if (currentScope == null) return super.visitSetValue(expression)

        return DeclarationIrBuilder(irContext, currentScope !!.scope.scopeOwnerSymbol).irComposite {

            val traceData = traceStateChangeBefore(stateVariable)

            + irBuilder.irSetValue(stateVariable.irProperty, expression.value, scopeReceiver())

            val index = stateVariable.rumElement.index
            val dirtyMask = airClass.dirtyMasks[index / RUI_STATE_VARIABLE_LIMIT]

            + irCallOp(
                dirtyMask.invalidate.symbol,
                irBuiltIns.unitType,
                IrGetValueImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, startScopeValue),
                (index % RUI_STATE_VARIABLE_LIMIT).toIrConst(irBuiltIns.longType)
            )

            traceStateChangeAfter(stateVariable, traceData)
        }
    }

    fun IrBlockBuilder.traceStateChangeBefore(stateVariable: AirStateVariable): IrVariable? {
        if (! irBuilder.context.withTrace) return null

        return irTemporary(irTraceGet(stateVariable, irBuilder.irThisReceiver()))
            .also { it.parent = currentFunction !!.irElement as IrFunction }
    }

    fun IrBlockBuilder.traceStateChangeAfter(stateVariable: AirStateVariable, traceData: IrVariable?) {
        if (traceData == null) return

        irBuilder.irTrace(
            "state change", listOf(
                irString("${stateVariable.irProperty.name.identifier}:"),
                irGet(traceData),
                irString(" ⇢ "),
                irTraceGet(stateVariable, irBuilder.irThisReceiver())
            )
        )
    }

    fun IrBlockBuilder.irTraceGet(stateVariable: AirStateVariable, receiver: IrExpression): IrExpression =
        stateVariable.irProperty.backingField
            ?.let { irGetField(receiver, it) }
            ?: irString("?")
}
