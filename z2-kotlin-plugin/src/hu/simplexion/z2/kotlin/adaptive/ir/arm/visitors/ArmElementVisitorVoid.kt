/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors

import hu.simplexion.z2.kotlin.adaptive.ir.arm.*

interface ArmElementVisitorVoid<out R> : ArmElementVisitor<R, Nothing?> {

    fun visitElement(element: ArmElement): R
    override fun visitElement(element: ArmElement, data: Nothing?) = visitElement(element)

    fun visitEntryPoint(armEntryPoint: ArmEntryPoint) = visitElement(armEntryPoint)
    override fun visitEntryPoint(armEntryPoint: ArmEntryPoint, data: Nothing?) = visitEntryPoint(armEntryPoint)

    fun visitClass(armClass: ArmClass) = visitElement(armClass)
    override fun visitClass(armClass: ArmClass, data: Nothing?) = visitClass(armClass)

    fun visitStateVariable(stateVariable: ArmStateVariable) = visitElement(stateVariable)
    override fun visitStateVariable(stateVariable: ArmStateVariable, data: Nothing?) = visitStateVariable(stateVariable)

    fun visitExternalStateVariable(stateVariable: ArmExternalStateVariable) = visitElement(stateVariable)
    override fun visitExternalStateVariable(stateVariable: ArmExternalStateVariable, data: Nothing?) = visitExternalStateVariable(stateVariable)

    fun visitInternalStateVariable(stateVariable: ArmInternalStateVariable) = visitElement(stateVariable)
    override fun visitInternalStateVariable(stateVariable: ArmInternalStateVariable, data: Nothing?) = visitInternalStateVariable(stateVariable)

    fun visitDirtyMask(dirtyMask: ArmDirtyMask) = visitElement(dirtyMask)
    override fun visitDirtyMask(dirtyMask: ArmDirtyMask, data: Nothing?) = visitDirtyMask(dirtyMask)

    fun visitStatement(statement: ArmRenderingStatement) = visitElement(statement)
    override fun visitStatement(statement: ArmRenderingStatement, data: Nothing?) = visitStatement(statement)

    fun visitSequence(statement: ArmSequence) = visitElement(statement)
    override fun visitSequence(statement: ArmSequence, data: Nothing?) = visitSequence(statement)

    fun visitCall(statement: ArmCall) = visitElement(statement)
    override fun visitCall(statement: ArmCall, data: Nothing?) = visitCall(statement)

    fun visitHigherOrderCall(statement: ArmHigherOrderCall) = visitElement(statement)
    override fun visitHigherOrderCall(statement: ArmHigherOrderCall, data: Nothing?) = visitHigherOrderCall(statement)

    fun visitParameterFunctionCall(statement: ArmParameterFunctionCall) = visitElement(statement)
    override fun visitParameterFunctionCall(statement: ArmParameterFunctionCall, data: Nothing?) = visitParameterFunctionCall(statement)

    fun visitWhen(statement: ArmWhen) = visitElement(statement)
    override fun visitWhen(statement: ArmWhen, data: Nothing?) = visitWhen(statement)

    fun visitForLoop(statement: ArmForLoop) = visitElement(statement)
    override fun visitForLoop(statement: ArmForLoop, data: Nothing?) = visitForLoop(statement)

    fun visitExpression(expression: ArmExpression) = visitElement(expression)
    override fun visitExpression(expression: ArmExpression, data: Nothing?) = visitExpression(expression)

    fun visitValueArgument(valueArgument: ArmValueArgument) = visitExpression(valueArgument)
    override fun visitValueArgument(valueArgument: ArmValueArgument, data: Nothing?) = visitValueArgument(valueArgument)

    fun visitHigherOrderArgument(higherOrderArgument: ArmHigherOrderArgument) = visitExpression(higherOrderArgument)
    override fun visitHigherOrderArgument(higherOrderArgument: ArmHigherOrderArgument, data: Nothing?) = visitHigherOrderArgument(higherOrderArgument)

    fun visitDeclaration(declaration: ArmDeclaration) = visitElement(declaration)
    override fun visitDeclaration(declaration: ArmDeclaration, data: Nothing?) = visitDeclaration(declaration)

    fun visitBranch(branch: ArmBranch) = visitElement(branch)
    override fun visitBranch(branch: ArmBranch, data: Nothing?) = visitBranch(branch)

}