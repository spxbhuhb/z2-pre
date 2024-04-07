/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.ir2arm

import hu.simplexion.z2.kotlin.adaptive.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.adaptive.ir.arm.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.dumpKotlinLike

/**
 * Transforms function parameters and top-level variable declarations into
 * state variables.
 *
 * @property  skipParameters  Skip the first N parameter of the original function when
 *                            converting the function parameters into external state variables.
 *                            Used for entry points when the first parameter is the adapter.
 */
class StateDefinitionTransform(
    private val armClass: ArmClass
) {

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

            ArmExternalStateVariable(
                armClass,
                stateVariableIndex,
                stateVariableIndex,
                valueParameter.name.identifier,
                valueParameter.type,
                valueParameter.symbol
            ).apply {
                register(valueParameter)
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

        check (name !in names) { "variable shadowing is not allowed:\n${declaration.dumpKotlinLike()}\n${declaration.dump()}" }

        stateVariableIndex ++
        names += name
        armClass.stateVariables += this
    }

}
