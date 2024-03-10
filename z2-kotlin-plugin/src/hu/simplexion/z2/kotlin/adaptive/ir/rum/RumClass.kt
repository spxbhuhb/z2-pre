/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.rum

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.ir2rum.BoundaryVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.rum2air.RumClass2Air
import hu.simplexion.z2.kotlin.adaptive.ir.util.toAdaptiveClassFqName
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.statements

class RumClass(
    val adaptiveContext: AdaptivePluginContext,
    val originalFunction: IrFunction,
    val parentScope: RumClass?,
) : RumElement {

    val fqName = originalFunction.toAdaptiveClassFqName(adaptiveContext, isAnonymous)
    val name = fqName.shortName()

    var compilationError: Boolean = false

    val isAnonymous
        get() = (parentScope != null)

    val boundary: RumBoundary = BoundaryVisitor(adaptiveContext).findBoundary(originalFunction)

    val originalStatements = checkNotNull(originalFunction.body?.statements) { "missing function body" }

    val initializerStatements = mutableListOf<IrStatement>()
    val renderingStatements = mutableListOf<IrStatement>()

    val stateVariables = mutableListOf<RumStateVariable>()
    val dirtyMasks = mutableListOf<RumDirtyMask>()

    lateinit var rendering: RumRenderingStatement

    fun toAir(context: AdaptivePluginContext): AirClass = RumClass2Air(context, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) {
        stateVariables.forEach { it.accept(visitor, data) }
        dirtyMasks.forEach { it.accept(visitor, data) }
        rendering.accept(visitor, data)
    }
}