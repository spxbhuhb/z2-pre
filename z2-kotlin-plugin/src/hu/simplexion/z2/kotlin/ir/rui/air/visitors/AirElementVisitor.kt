/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.air.visitors

import hu.simplexion.z2.kotlin.ir.rui.air.*

interface AirElementVisitor<out R, in D> {

    fun visitElement(element: AirElement, data: D): R

    fun visitEntryPoint(airEntryPoint: AirEntryPoint, data: D) = visitElement(airEntryPoint, data)

    fun visitClass(airClass: AirClass, data: D) = visitElement(airClass, data)

    fun visitProperty(property: AirProperty, data: D) = visitElement(property, data)

    fun visitStateVariable(stateVariable: AirStateVariable, data: D) = visitProperty(stateVariable, data)

    fun visitDirtyMask(dirtyMask: AirDirtyMask, data: D) = visitProperty(dirtyMask, data)

    fun visitFunction(function: AirFunction, data: D) = visitElement(function, data)

    fun visitBuilder(builder: AirBuilder, data: D) = visitFunction(builder, data)

    fun visitBuilderBlock(builder: AirBuilderBlock, data: D) = visitBuilder(builder, data)
    fun visitBuilderCall(builder: AirBuilderCall, data: D) = visitBuilder(builder, data)
    fun visitBuilderForLoop(builder: AirBuilderForLoop, data: D) = visitBuilder(builder, data)
    fun visitBuilderWhen(builder: AirBuilderWhen, data: D) = visitBuilder(builder, data)

    fun visitExternalPatch(externalPatch: AirExternalPatch, data: D) = visitFunction(externalPatch, data)

    fun visitExternalPatchBlock(externalPatch: AirExternalPatchBlock, data: D) = visitExternalPatch(externalPatch, data)
    fun visitExternalPatchCall(externalPatch: AirExternalPatchCall, data: D) = visitExternalPatch(externalPatch, data)
    fun visitExternalPatchForLoop(externalPatch: AirExternalPatchForLoop, data: D) = visitExternalPatch(externalPatch, data)
    fun visitExternalPatchWhen(externalPatch: AirExternalPatchWhen, data: D) = visitExternalPatch(externalPatch, data)

}