/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.ir2arm

import hu.simplexion.z2.kotlin.adaptive.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.*
import hu.simplexion.z2.kotlin.adaptive.ir.util.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.isFunction
import org.jetbrains.kotlin.ir.util.statements

/**
 * Transforms an original function into a [ArmClass]. This is a somewhat complex transformation.
 *
 * Calls [StateDefinitionTransform] to:
 *
 *   - convert function parameters into [ArmExternalStateVariable] instances
 *   - convert function variables are into [ArmInternalStateVariable] instances
 *
 * Transforms IR structures such as loops, branches and calls into [ArmRenderingStatement] instances.
 * The type of the statement corresponds with the language construct.
 *
 * Calls [DependencyVisitor] to build dependencies for each block.
 */
class IrFunction2ArmClass(
    val adaptiveContext: AdaptivePluginContext,
    val irFunction: IrFunction
) : AdaptiveNonAnnotationBasedExtension {

    lateinit var armClass: ArmClass

    var fragmentIndex = 0
        get() = field ++

    var supportIndex = 0
        get() = field ++

    val states: Stack<ArmState> = mutableListOf()
    val closures: Stack<ArmClosure> = mutableListOf()

    val closure: ArmClosure
        get() = closures.peek()

    fun transform(): ArmClass {
        armClass = ArmClass(adaptiveContext, irFunction)

        StateDefinitionTransform(armClass, 0).transform()

        states.push(armClass.stateVariables)
        closures.push(armClass.stateVariables)

        transformBlock(armClass.originalRenderingStatements)

        armClass.rendering.sortBy { it.index }

        adaptiveContext.armClasses += armClass

        return armClass
    }

    fun IrElement.dependencies(): List<ArmStateVariable> {
        val visitor = DependencyVisitor(states.peek())
        accept(visitor, null)
        return visitor.dependencies
    }

    fun ArmRenderingStatement.add(): ArmRenderingStatement {
        armClass.rendering += this
        return this
    }

    fun transformStatement(statement: IrStatement): ArmRenderingStatement =
        when (statement) {
            is IrBlock -> {
                when (statement.origin) {
                    IrStatementOrigin.FOR_LOOP -> transformLoop(statement)
                    IrStatementOrigin.WHEN -> transformWhen(statement)
                    else -> throw IllegalStateException("unsupported rendering structure")
                }
            }

            is IrCall -> transformCall(statement)

            is IrWhen -> transformWhen(statement)

            is IrReturn -> ArmSequence(armClass, fragmentIndex, closure, statement.startOffset, emptyList()).add() // TODO better check on return statement

            else -> throw IllegalStateException("invalid rendering statement: ${statement.dumpKotlinLike()}")

        }


    // ---------------------------------------------------------------------------
    // Block (may be whatever block: when, if, loop)
    // ---------------------------------------------------------------------------

    fun transformBlock(statements : List<IrStatement>): ArmRenderingStatement {
        return if (statements.size == 1) {
            transformStatement(statements.first())
        } else {
            val sequenceIndex = fragmentIndex

            ArmSequence(
                armClass, sequenceIndex, closure,
                armClass.boundary.startOffset,
                statements.map { transformStatement(it) }
            ).add()
        }
    }

    // ---------------------------------------------------------------------------
    // For Loop
    // ---------------------------------------------------------------------------

    fun transformLoop(statement: IrBlock): ArmRenderingStatement {

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

        val iterator = transformDeclaration(irIterator, ArmDeclarationOrigin.FOR_LOOP_ITERATOR)
        val loopVariable = transformDeclaration(irLoopVariable, ArmDeclarationOrigin.FOR_LOOP_VARIABLE)

        val rendering = transformStatement(block)

        return ArmLoop(
            armClass,
            fragmentIndex,
            closure,
            statement.startOffset,
            iterator,
            condition,
            loopVariable,
            rendering
        ).add()
    }

    fun transformDeclaration(declaration: IrDeclaration, origin: ArmDeclarationOrigin): ArmDeclaration =
        when (declaration) {
            is IrValueDeclaration -> ArmDeclaration(armClass, declaration, origin, declaration.dependencies())
            else -> throw IllegalStateException("invalid declaration in rendering: ${declaration.dumpKotlinLike()}")
        }

    // ---------------------------------------------------------------------------
    // Call
    // ---------------------------------------------------------------------------

    fun transformCall(irCall: IrCall): ArmRenderingStatement {

        if (! irCall.isAdaptive(adaptiveContext)) {
            throw IllegalStateException("non-adaptive call in rendering: ${irCall.dumpKotlinLike()}")
        }

        val armCall = ArmCall(armClass, fragmentIndex, closure, irCall)
        val valueParameters = irCall.symbol.owner.valueParameters

        for (argumentIndex in 0 until irCall.valueArgumentsCount) {
            val parameter = valueParameters[argumentIndex]
            val expression = irCall.getValueArgument(argumentIndex) ?: continue
            armCall.arguments += transformValueArgument(argumentIndex, parameter, expression)
        }

        armCall.hasInvokeBranch = armCall.arguments.any { it is ArmSupportFunctionArgument }

        return armCall.add()
    }

    fun transformValueArgument(
        argumentIndex: Int,
        parameter: IrValueParameter,
        expression: IrExpression
    ): ArmValueArgument =
        when {
            parameter.isAdaptive() -> {
                if (expression is IrFunctionExpression) {
                    ArmFragmentFactoryArgument(armClass, fragmentIndex, expression, expression.dependencies()).also {
                        transformFragmentFactoryArgument(it, expression)
                    }
                } else {
                    ArmValueArgument(armClass, argumentIndex, expression, expression.dependencies())
                }
            }

            parameter.type.isFunction() -> {
                if (expression is IrFunctionExpression) {
                    ArmSupportFunctionArgument(armClass, argumentIndex, supportIndex, closure, expression, expression.dependencies())
                } else {
                    ArmValueArgument(armClass, argumentIndex, expression, expression.dependencies())
                }
            }

            else -> {
                ArmValueArgument(armClass, argumentIndex, expression, expression.dependencies())
            }
        }

    fun transformFragmentFactoryArgument(
        fragmentFactory: ArmFragmentFactoryArgument,
        expression: IrFunctionExpression
    ) {
        // add the anonymous function parameters to the closure

        var stateVariableIndex = states.last().last().indexInClosure + 1

        states.push(
            expression.function.valueParameters.mapIndexed { indexInState, parameter ->
                ArmExternalStateVariable(armClass, stateVariableIndex ++, indexInState, parameter)
            }
        )

        closures.push(states.flatten())

        check(closure.size < ADAPTIVE_STATE_VARIABLE_LIMIT) { "maximum number of state variables in any closure is $ADAPTIVE_STATE_VARIABLE_LIMIT" }

        // transform the anonymous function itself with the new closure

        fragmentFactory.closure = closure

        expression.function.body?.statements?.forEach {
            transformStatement(it)
        }

        // remove the anonymous function parameters from the closure

        closures.pop()
        states.pop()
    }

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
    fun transformWhen(statement: IrBlock): ArmRenderingStatement {
        // TODO convert checks into non-exception throwing, but contracting functions
        check(statement.statements.size == 2)

        val subject = statement.statements[0]
        val evaluation = statement.statements[1]

        check(subject is IrVariable)
        check(evaluation is IrWhen && evaluation.origin == IrStatementOrigin.WHEN)

        return transformWhen(evaluation, subject)
    }

    fun transformWhen(statement: IrWhen, subject: IrVariable? = null): ArmRenderingStatement {

        val armSelect = ArmSelect(armClass, fragmentIndex, closure, statement.startOffset)

        armSelect.branches += statement.branches.map { irBranch ->
            ArmBranch(
                armClass,
                transformExpression(irBranch.condition, ArmExpressionOrigin.BRANCH_CONDITION),
                transformStatement(irBranch.result)
            )
        }

        return armSelect.add()
    }

    fun transformExpression(condition: IrExpression, origin: ArmExpressionOrigin): ArmExpression {
        // add "else" if the last condition is not a constant true
        return ArmExpression(armClass, condition, origin, condition.dependencies())
    }

}