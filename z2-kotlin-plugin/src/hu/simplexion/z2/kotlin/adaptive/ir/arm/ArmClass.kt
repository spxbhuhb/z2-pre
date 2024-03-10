/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmClass2Air
import hu.simplexion.z2.kotlin.adaptive.ir.ir2arm.BoundaryVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.util.toAdaptiveClassFqName
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.statements

class ArmClass(
    val adaptiveContext: AdaptivePluginContext,
    val originalFunction: IrFunction,
    val parentScope: ArmClass?,
) : ArmElement {

    val fqName = originalFunction.toAdaptiveClassFqName(adaptiveContext, isAnonymous)
    val name = fqName.shortName()

    var compilationError: Boolean = false

    val isAnonymous
        get() = (parentScope != null)

    val boundary: ArmBoundary = BoundaryVisitor(adaptiveContext).findBoundary(originalFunction)

    val originalStatements = checkNotNull(originalFunction.body?.statements) { "missing function body" }

    val initializerStatements = mutableListOf<IrStatement>()
    val renderingStatements = mutableListOf<IrStatement>()

    val stateVariables = mutableListOf<ArmStateVariable>()
    val dirtyMasks = mutableListOf<ArmDirtyMask>()

    lateinit var rendering: ArmRenderingStatement

    fun toAir(context: AdaptivePluginContext): AirClass = ArmClass2Air(context, this).toAir()

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        stateVariables.forEach { it.accept(visitor, data) }
        dirtyMasks.forEach { it.accept(visitor, data) }
        rendering.accept(visitor, data)
    }
}