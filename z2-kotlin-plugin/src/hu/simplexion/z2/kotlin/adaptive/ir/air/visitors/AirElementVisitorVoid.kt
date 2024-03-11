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

    fun visitProperty(property: AirProperty) = visitElement(property)
    override fun visitProperty(property: AirProperty, data: Nothing?) = visitProperty(property)

    fun visitStateVariable(stateVariable: AirStateVariable) = visitProperty(stateVariable)
    override fun visitStateVariable(stateVariable: AirStateVariable, data: Nothing?) = visitStateVariable(stateVariable)

    fun visitDirtyMask(dirtyMask: AirDirtyMask) = visitProperty(dirtyMask)
    override fun visitDirtyMask(dirtyMask: AirDirtyMask, data: Nothing?) = visitDirtyMask(dirtyMask)

    fun visitFunction(function: AirFunction) = visitElement(function)
    override fun visitFunction(function: AirFunction, data: Nothing?) = visitFunction(function)

    fun visitBuilder(builder: AirBuilder) = visitFunction(builder)
    override fun visitBuilder(builder: AirBuilder, data: Nothing?) = visitBuilder(builder)

    fun visitBuilderSequence(builder: AirBuilderSequence) = visitBuilder(builder)
    override fun visitBuilderSequence(builder: AirBuilderSequence, data: Nothing?) = visitBuilderSequence(builder)

    fun visitBuilderCall(builder: AirBuilderCall) = visitBuilder(builder)
    override fun visitBuilderCall(builder: AirBuilderCall, data: Nothing?) = visitBuilderCall(builder)

    fun visitBuilderForLoop(builder: AirBuilderForLoop) = visitBuilder(builder)
    override fun visitBuilderForLoop(builder: AirBuilderForLoop, data: Nothing?) = visitBuilderForLoop(builder)

    fun visitBuilderWhen(builder: AirBuilderWhen) = visitBuilder(builder)
    override fun visitBuilderWhen(builder: AirBuilderWhen, data: Nothing?) = visitBuilderWhen(builder)

    fun visitExternalPatch(externalPatch: AirExternalPatch) = visitFunction(externalPatch)
    override fun visitExternalPatch(externalPatch: AirExternalPatch, data: Nothing?) = visitExternalPatch(externalPatch)

    fun visitExternalPatchSequence(externalPatch: AirExternalPatchSequence) = visitExternalPatch(externalPatch)
    override fun visitExternalPatchSequence(externalPatch: AirExternalPatchSequence, data: Nothing?) = visitExternalPatchSequence(externalPatch)

    fun visitExternalPatchCall(externalPatch: AirExternalPatchCall) = visitExternalPatch(externalPatch)
    override fun visitExternalPatchCall(externalPatch: AirExternalPatchCall, data: Nothing?) = visitExternalPatchCall(externalPatch)

    fun visitExternalPatchForLoop(externalPatch: AirExternalPatchForLoop) = visitExternalPatch(externalPatch)
    override fun visitExternalPatchForLoop(externalPatch: AirExternalPatchForLoop, data: Nothing?) = visitExternalPatchForLoop(externalPatch)

    fun visitExternalPatchWhen(externalPatch: AirExternalPatchWhen) = visitExternalPatch(externalPatch)
    override fun visitExternalPatchWhen(externalPatch: AirExternalPatchWhen, data: Nothing?) = visitExternalPatchWhen(externalPatch)

}