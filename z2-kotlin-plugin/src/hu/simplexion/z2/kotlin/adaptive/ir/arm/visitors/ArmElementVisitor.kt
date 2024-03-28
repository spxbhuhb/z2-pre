/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors

import hu.simplexion.z2.kotlin.adaptive.ir.arm.*

interface ArmElementVisitor<out R, in D> {
    fun visitElement(element: ArmElement, data: D): R

    fun visitEntryPoint(armEntryPoint: ArmEntryPoint, data: D) = visitElement(armEntryPoint, data)

    fun visitClass(armClass: ArmClass, data: D) = visitElement(armClass, data)

    fun visitStateVariable(stateVariable: ArmStateVariable, data: D) = visitElement(stateVariable, data)
    fun visitExternalStateVariable(stateVariable: ArmExternalStateVariable, data: D) = visitStateVariable(stateVariable, data)
    fun visitInternalStateVariable(stateVariable: ArmInternalStateVariable, data: D) = visitStateVariable(stateVariable, data)

    fun visitDirtyMask(dirtyMask: ArmDirtyMask, data: D) = visitElement(dirtyMask, data)

    fun visitStatement(statement: ArmRenderingStatement, data: D) = visitElement(statement, data)
    fun visitSequence(statement: ArmSequence, data: D) = visitStatement(statement, data)
    fun visitCall(statement: ArmCall, data: D) = visitStatement(statement, data)
    fun visitCallbackFunctionCall(statement: ArmSupportFunctionCall, data: D) = visitStatement(statement, data)
    fun visitHigherOrderCall(statement: ArmHigherOrderCall, data: D) = visitStatement(statement, data)
    fun visitParameterFunctionCall(statement: ArmLowerOrderFunctionCall, data: D) = visitStatement(statement, data)
    fun visitWhen(statement: ArmSelect, data: D) = visitStatement(statement, data)
    fun visitForLoop(statement: ArmLoop, data: D) = visitStatement(statement, data)

    fun visitExpression(expression: ArmExpression, data: D) = visitElement(expression, data)
    fun visitValueArgument(valueArgument: ArmValueArgument, data: D) = visitElement(valueArgument, data)
    fun visitSupportFunctionArgument(supportFunctionArgument: ArmSupportFunctionArgument, data: D) = visitElement(supportFunctionArgument, data)
    fun visitHigherOrderArgument(higherOrderArgument: ArmFragmentFactoryArgument, data: D) = visitElement(higherOrderArgument, data)

    fun visitDeclaration(declaration: ArmDeclaration, data: D) = visitElement(declaration, data)

    fun visitBranch(branch: ArmBranch, data: D) = visitElement(branch, data)

}