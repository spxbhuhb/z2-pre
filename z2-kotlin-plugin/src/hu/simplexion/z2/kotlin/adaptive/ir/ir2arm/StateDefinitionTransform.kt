/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.ir2arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmExternalStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmInternalStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.diagnostics.ErrorsAdaptive.ADAPTIVE_IR_RENDERING_VARIABLE
import hu.simplexion.z2.kotlin.adaptive.ir.diagnostics.ErrorsAdaptive.ADAPTIVE_IR_STATE_VARIABLE_SHADOW
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable

/**
 * Transforms function parameters and top-level variable declarations into
 * state variables.
 *
 * @property  skipParameters  Skip the first N parameter of the original function when
 *                            converting the function parameters into external state variables.
 *                            Used for entry points when the first parameter is the adapter.
 */
class StateDefinitionTransform(
    private val armClass: ArmClass,
    private val skipParameters: Int
) {

    val names = mutableListOf<String>()

    var stateVariableIndex = 0

    fun transform() {

        armClass.originalFunction.valueParameters.forEachIndexed { index, valueParameter ->

            if (index < skipParameters) return@forEachIndexed

            ArmExternalStateVariable(armClass, stateVariableIndex, stateVariableIndex, valueParameter).register(valueParameter)

        }

        armClass.originalStatements.forEach { statement ->
            when {
                statement is IrVariable -> {
                    ArmInternalStateVariable(armClass, stateVariableIndex, stateVariableIndex, statement).register(statement)
                }

                statement.startOffset < armClass.boundary.startOffset -> {
                    armClass.originalInitializationStatements += statement
                }

                else -> {
                    armClass.originalRenderingStatements += statement
                }
            }
        }
    }

    fun ArmStateVariable.register(declaration: IrDeclaration) {

        if (declaration.startOffset >= armClass.boundary.startOffset) {
            ADAPTIVE_IR_RENDERING_VARIABLE.report(armClass, declaration)
            return
        }

        if (name in names) {
            // variable shadowing is a bad practice anyway, no big loss to forbid it
            ADAPTIVE_IR_STATE_VARIABLE_SHADOW.report(armClass, declaration)
            return
        }

        stateVariableIndex++
        names += name
        armClass.stateVariables += this
    }

}
