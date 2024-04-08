/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.ir2arm

import hu.simplexion.z2.kotlin.adaptive.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.*
import hu.simplexion.z2.kotlin.adaptive.ir.util.AdaptiveNonAnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.deepCopyWithVariables
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.dumpKotlinLike


/**
 * Transforms function parameters and top-level variable declarations into
 * state variables.
 */
class StateDefinitionTransform(
    override val adaptiveContext: AdaptivePluginContext,
    private val armClass: ArmClass
) : AdaptiveNonAnnotationBasedExtension {

    val names = mutableListOf<String>()

    // incremented by `register`
    var stateVariableIndex = 0

    fun IrElement.dependencies(): List<ArmStateVariable> {
        val visitor = DependencyVisitor(armClass.stateVariables)
        accept(visitor, null)
        return visitor.dependencies
    }

    fun transform() {
        transformParameters()
        transformStatements()
    }

    fun transformParameters() {
        armClass.originalFunction.valueParameters.forEach { valueParameter ->

            // access selector function is not part of the state, it is for the plugin to know
            // which state variable to access
            // TODO add FIR checker to make sure the selector and the binding type arguments are the same

            if (valueParameter.type.isAccessSelector(armClass.stateVariables.lastOrNull()?.type)) return@forEach

            ArmExternalStateVariable(
                armClass,
                stateVariableIndex,
                stateVariableIndex,
                valueParameter.name.identifier,
                valueParameter.type,
                valueParameter.symbol
            ).apply {
                register(valueParameter)

                val defaultValue = valueParameter.defaultValue ?: return@forEach

                armClass.stateDefinitionStatements +=
                    ArmDefaultValueStatement(
                        indexInState,
                        defaultValue.expression.deepCopyWithVariables(),
                        defaultValue.expression.dependencies()
                    )
            }
        }
    }

    fun transformStatements() {
        armClass.originalStatements.forEach { statement ->
            when {
                statement is IrVariable -> {
                    armClass.stateDefinitionStatements +=
                        ArmInternalStateVariable(armClass, stateVariableIndex, stateVariableIndex, statement, statement.dependencies()).apply {
                            register(statement)
                        }
                }

                statement.startOffset < armClass.boundary.startOffset -> {
                    armClass.stateDefinitionStatements += ArmStateDefinitionStatement(statement, statement.dependencies())
                }

                else -> {
                    armClass.originalRenderingStatements += statement
                }
            }
        }
    }

    fun ArmStateVariable.register(declaration: IrDeclaration) {

        check(stateVariableIndex < ADAPTIVE_STATE_VARIABLE_LIMIT) { "maximum number of state variables is $ADAPTIVE_STATE_VARIABLE_LIMIT" }

        check(declaration.startOffset < armClass.boundary.startOffset) { "declaration in rendering at:\n${declaration.dumpKotlinLike()}\n${declaration.dump()}" }

        check(name !in names) { "variable shadowing is not allowed:\n${declaration.dumpKotlinLike()}\n${declaration.dump()}" }

        stateVariableIndex ++
        names += name
        armClass.stateVariables += this
    }

}
