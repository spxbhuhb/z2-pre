/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.air.visitors

import hu.simplexion.z2.kotlin.adaptive.ir.air.*

interface AirElementVisitor<out R, in D> {

    fun visitElement(element: AirElement, data: D): R

    fun visitEntryPoint(airEntryPoint: AirEntryPoint, data: D) = visitElement(airEntryPoint, data)

    fun visitClass(airClass: AirClass, data: D) = visitElement(airClass, data)

    fun visitStateVariable(stateVariable: AirStateVariable, data: D) = visitElement(stateVariable, data)

    fun visitBuildBranch(branch: AirBuildBranch, data: D) = visitElement(branch, data)

    fun visitPatchBranch(branch: AirPatchDescendantBranch, data: D) = visitElement(branch, data)

    fun visitInvokeBranch(branch: AirInvokeBranch, data: D) = visitElement(branch, data)

}