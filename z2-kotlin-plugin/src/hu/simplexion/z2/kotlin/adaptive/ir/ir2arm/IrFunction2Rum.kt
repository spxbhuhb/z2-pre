/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.ir2arm

import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.diagnostics.ErrorsAdaptive.ADAPTIVE_IR_INVALID_RENDERING_STATEMENT
import hu.simplexion.z2.kotlin.adaptive.diagnostics.ErrorsAdaptive.ADAPTIVE_IR_RENDERING_INVALID_DECLARATION
import hu.simplexion.z2.kotlin.adaptive.diagnostics.ErrorsAdaptive.RIU_IR_RENDERING_NON_ADAPTIVE_CALL
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.*
import hu.simplexion.z2.kotlin.adaptive.ir.util.AdaptiveAnnotationBasedExtension
import hu.simplexion.z2.kotlin.adaptive.ir.util.isParameterCall
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.psi.KtModifierListOwner

/**
 * Transforms an original function into a [ArmClass]. This is a somewhat complex transformation.
 *
 * Calls [StateDefinitionTransform] to:
 *
 *   - convert function parameters into [ArmExternalStateVariable] instances
 *   - convert function variables are into [ArmInternalStateVariable] instances
 *
 * Transforms IR structures such as loops, branches and calls into [ArmBlock] instances.
 * The type of the block corresponds with the language construct:
 *
 * - code block: [ArmBlock]
 * - `for` : [ArmForLoop]
 * - `if`, `when`: [ArmWhen]
 * - function call: [ArmCall]
 * - higher order function call: [ArmHigherOrderCall]
 *
 * Calls [DependencyVisitor] to build dependencies for each block.
 */
class IrFunction2Arm(
    val adaptiveContext: AdaptivePluginContext,
    val irFunction: IrFunction,
    val skipParameters: Int,
    val scope: ArmClass? = null
) : AdaptiveAnnotationBasedExtension {

    lateinit var armClass: ArmClass

    var blockIndex = 0
        get() = field ++

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        listOf(Strings.ADAPTIVE_ANNOTATION)

    fun IrElement.dependencies(): List<ArmStateVariable> {
        val visitor = DependencyVisitor(adaptiveContext, armClass)
        accept(visitor, null)
        return visitor.dependencies
    }

    fun transform(): ArmClass {
        armClass = ArmClass(adaptiveContext, irFunction, scope)

        StateDefinitionTransform(armClass, skipParameters).transform()

        transformRoot()

        adaptiveContext.armClasses += armClass

        return armClass
    }

    fun transformRoot() {
        val statements = armClass.originalStatements

        val startOffset = armClass.originalFunction.startOffset
        val endOffset = armClass.originalFunction.endOffset

        val renderingBlock = IrBlockImpl(startOffset, endOffset, adaptiveContext.irContext.irBuiltIns.unitType)
        renderingBlock.statements.addAll(statements.subList(armClass.boundary.statementIndex, statements.size))

        // if this is a single statement, we don't need the surrounding block
        // TODO check if this is the right place for the optimization
        armClass.rendering = transformBlock(renderingBlock).let {
            if (it.statements.size == 1) {
                it.statements[0]
            } else {
                it
            }
        }
    }

    fun transformBlock(expression: IrBlock): ArmBlock {

        val armBlock = ArmBlock(armClass, blockIndex, expression)

        expression.statements.forEach { statement ->
            when (statement) {
                is IrBlock -> {
                    when (statement.origin) {
                        IrStatementOrigin.FOR_LOOP -> transformForLoop(statement)
                        IrStatementOrigin.WHEN -> transformWhen(statement)
                        else -> ADAPTIVE_IR_INVALID_RENDERING_STATEMENT.report(armClass, statement)
                    }
                }

                is IrCall -> transformCall(statement)

                is IrWhen -> transformWhen(statement)

                else -> ADAPTIVE_IR_INVALID_RENDERING_STATEMENT.report(armClass, statement)

            }?.let {
                armBlock.statements += it
            }
        }

        return armBlock
    }

    // ---------------------------------------------------------------------------
    // For Loop
    // ---------------------------------------------------------------------------

    fun transformForLoop(statement: IrBlock): ArmForLoop? {

        // BLOCK type=kotlin.Unit origin=FOR_LOOP
        //          VAR FOR_LOOP_ITERATOR name:tmp0_iterator type:kotlin.collections.IntIterator [val]
        //            CALL 'public open fun iterator (): kotlin.collections.IntIterator [fake_override,operator] declared in kotlin.ranges.IntRange' type=kotlin.collections.IntIterator origin=FOR_LOOP_ITERATOR
        //              $this: CALL 'public final fun rangeTo (other: kotlin.Int): kotlin.ranges.IntRange [operator] declared in kotlin.Int' type=kotlin.ranges.IntRange origin=RANGE
        //                $this: CONST Int type=kotlin.Int value=0
        //                other: CONST Int type=kotlin.Int value=10
        //          WHILE label=null origin=FOR_LOOP_INNER_WHILE
        //            condition: CALL 'public abstract fun hasNext (): kotlin.Boolean [fake_override,operator] declared in kotlin.collections.IntIterator' type=kotlin.Boolean origin=FOR_LOOP_HAS_NEXT
        //              $this: GET_VAR 'val tmp0_iterator: kotlin.collections.IntIterator [val] declared in hu.simplexion.adaptive.kotlin.plugin.successes.Basic' type=kotlin.collections.IntIterator origin=null
        //            body: BLOCK type=kotlin.Unit origin=FOR_LOOP_INNER_WHILE
        //              VAR FOR_LOOP_VARIABLE name:i type:kotlin.Int [val]
        //                CALL 'public final fun next (): kotlin.Int [operator] declared in kotlin.collections.IntIterator' type=kotlin.Int origin=FOR_LOOP_NEXT
        //                  $this: GET_VAR 'val tmp0_iterator: kotlin.collections.IntIterator [val] declared in hu.simplexion.adaptive.kotlin.plugin.successes.Basic' type=kotlin.collections.IntIterator origin=null
        //              BLOCK type=kotlin.Unit origin=null
        //                CALL 'public final fun P1 (p0: kotlin.Int): kotlin.Unit declared in hu.simplexion.adaptive.kotlin.plugin' type=kotlin.Unit origin=null
        //                  p0: GET_VAR 'val i: kotlin.Int [val] declared in hu.simplexion.adaptive.kotlin.plugin.successes.Basic' type=kotlin.Int origin=null

        // TODO convert checks into non-exception throwing, but contracting functions
        check(statement.statements.size == 2)

        val irIterator = statement.statements[0]
        val loop = statement.statements[1]

        check(irIterator is IrValueDeclaration && irIterator.origin == IrDeclarationOrigin.FOR_LOOP_ITERATOR)
        check(loop is IrWhileLoop && loop.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)

        val condition = transformExpression(loop.condition, ArmExpressionOrigin.FOR_LOOP_CONDITION)

        val body = loop.body

        check(body is IrBlock && body.origin == IrStatementOrigin.FOR_LOOP_INNER_WHILE)
        check(body.statements.size == 2)

        val irLoopVariable = body.statements[0]
        val block = body.statements[1]

        check(irLoopVariable is IrVariable)
        check((block is IrBlock && block.origin == null) || block is IrCall) // TODO think for loop check details

        val iterator = transformDeclaration(irIterator, ArmDeclarationOrigin.FOR_LOOP_ITERATOR) ?: return null
        val loopVariable = transformDeclaration(irLoopVariable, ArmDeclarationOrigin.FOR_LOOP_VARIABLE) ?: return null

        val rendering = transformRenderingExpression(block as IrExpression)
            ?: return null

        return ArmForLoop(
            armClass, blockIndex, statement,
            iterator,
            condition,
            loopVariable,
            rendering
        )
    }

    fun transformDeclaration(declaration: IrDeclaration, origin: ArmDeclarationOrigin): ArmDeclaration? =
        when (declaration) {
            is IrValueDeclaration -> ArmDeclaration(armClass, declaration, origin, declaration.dependencies())
            else -> ADAPTIVE_IR_RENDERING_INVALID_DECLARATION.report(armClass, declaration)
        }

    // ---------------------------------------------------------------------------
    // Call
    // ---------------------------------------------------------------------------

    val IrCall.isHigherOrder: Boolean
        get() {
            // TODO check if caching for higher order function symbols has positive impact on compilation performance
            // TODO default parameter values for higher order calls
            for (index in 0 until valueArgumentsCount) {
                val va = getValueArgument(index) ?: continue
                if (va !is IrFunctionExpression) continue // this is just a speed to avoid the line below when possible
                if (this.symbol.owner.valueParameters.firstOrNull { it.isAnnotatedWithAdaptive() } == null) continue
                return true
            }
            return false
        }

    fun transformCall(statement: IrCall): ArmCall? {

        if (! statement.isAnnotatedWithAdaptive()) {
            return RIU_IR_RENDERING_NON_ADAPTIVE_CALL.report(armClass, statement)
        }

        return if (statement.isHigherOrder) {
            transformHigherOrderCall(statement)
        } else {
            transformSimpleCall(statement)
        }
    }

    fun transformSimpleCall(statement: IrCall): ArmCall {

        val armCall = if (statement.isParameterCall) {
            ArmParameterFunctionCall(armClass, blockIndex, statement)
        } else {
            ArmCall(armClass, blockIndex, statement)
        }

        for (index in 0 until statement.valueArgumentsCount) {
            val expression = statement.getValueArgument(index) ?: continue
            armCall.valueArguments += transformValueArgument(index, expression)
        }

        return armCall
    }

    fun transformValueArgument(index: Int, expression: IrExpression): ArmExpression {
        return ArmValueArgument(armClass, index, expression, expression.dependencies())
    }

    fun transformHigherOrderCall(irCall: IrCall): ArmHigherOrderCall {
        val armHigherOrderCall = ArmHigherOrderCall(armClass, blockIndex, irCall)

        val calleeArguments = irCall.symbol.owner.valueParameters // arguments of the higher order function

        for (index in 0 until irCall.valueArgumentsCount) {
            val expression = irCall.getValueArgument(index) ?: continue // TODO handle parameter default values

            armHigherOrderCall.valueArguments +=
                if (calleeArguments[index].isAnnotatedWithAdaptive() && expression is IrFunctionExpression) {
                    transformHigherOrderArgument(index, expression)
                } else {
                    transformValueArgument(index, expression)
                }
        }

        return armHigherOrderCall
    }

    /**
     * This is actually a very complicated and costly transformation as it calls [IrFunction2Arm]
     * recursively as long as there are higher order function calls inside the higher order function call.
     */
    fun transformHigherOrderArgument(index: Int, expression: IrFunctionExpression): ArmExpression =
        ArmHigherOrderArgument(
            IrFunction2Arm(adaptiveContext, expression.function, 0, armClass).transform(),
            index, // the parameter index of the parameter function at the given call site
            expression, // the code of the parameter function
            expression.dependencies(),
        )

    // ---------------------------------------------------------------------------
    // When
    // ---------------------------------------------------------------------------

    /**
     * Transforms a `when` with a subject variable like:
     *
     * ```kotlin
     * when (b) {
     *   // ...
     * }
     * ```
     */
    fun transformWhen(statement: IrBlock): ArmWhen? {
        // TODO convert checks into non-exception throwing, but contracting functions
        check(statement.statements.size == 2)

        val subject = statement.statements[0]
        val evaluation = statement.statements[1]

        check(subject is IrVariable)
        check(evaluation is IrWhen && evaluation.origin == IrStatementOrigin.WHEN)

        return transformWhen(evaluation, subject)
    }

    fun transformWhen(statement: IrWhen, subject: IrVariable? = null): ArmWhen? {
        val adaptiveWhen = ArmWhen(armClass, blockIndex, subject, statement)

        statement.branches.forEach { irBranch ->
            adaptiveWhen.branches += transformBranch(irBranch) ?: return null
        }

        return adaptiveWhen
    }

    fun transformBranch(branch: IrBranch): ArmBranch? {
        val rendering = transformRenderingExpression(branch.result)
            ?: return null

        return ArmBranch(
            armClass, blockIndex,
            branch,
            transformExpression(branch.condition, ArmExpressionOrigin.BRANCH_CONDITION),
            rendering,
        )
    }

    fun transformExpression(expression: IrExpression, origin: ArmExpressionOrigin): ArmExpression {
        return ArmExpression(armClass, expression, origin, expression.dependencies())
    }

    fun transformRenderingExpression(expression: IrExpression): ArmRenderingStatement? {
        return when (expression) {
            is IrCall -> transformCall(expression)
            is IrBlock -> transformBlock(expression)
            is IrWhen -> transformWhen(expression)
            else -> {
                ADAPTIVE_IR_RENDERING_INVALID_DECLARATION.report(armClass, expression)
                null
            }
        }
    }

}