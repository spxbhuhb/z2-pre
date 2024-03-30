/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.air.visitors

import hu.simplexion.z2.kotlin.adaptive.ir.air.*

interface AirElementVisitorVoid<out R> : AirElementVisitor<R, Nothing?> {

    fun visitElement(element: AirElement): R
    override fun visitElement(element: AirElement, data: Nothing?) = visitElement(element)

    fun visitEntryPoint(airEntryPoint: AirEntryPoint) = visitElement(airEntryPoint)
    override fun visitEntryPoint(airEntryPoint: AirEntryPoint, data: Nothing?) = visitEntryPoint(airEntryPoint)

    fun visitClass(airClass: AirClass) = visitElement(airClass)
    override fun visitClass(airClass: AirClass, data: Nothing?) = visitClass(airClass)

    fun visitStateVariable(stateVariable: AirStateVariable) = visitElement(stateVariable)
    override fun visitStateVariable(stateVariable: AirStateVariable, data: Nothing?) = visitStateVariable(stateVariable)

    fun visitBuildBranch(branch: AirBuildBranch) = visitElement(branch)
    override fun visitBuildBranch(branch: AirBuildBranch, data: Nothing?) = visitBuildBranch(branch)

    fun visitPatchBranch(branch: AirPatchBranch) = visitElement(branch)
    override fun visitPatchBranch(branch: AirPatchBranch, data: Nothing?) = visitPatchBranch(branch)

    fun visitInvokeBranch(branch: AirInvokeBranch) = visitElement(branch)
    override fun visitInvokeBranch(branch: AirInvokeBranch, data: Nothing?) = visitInvokeBranch(branch)

}