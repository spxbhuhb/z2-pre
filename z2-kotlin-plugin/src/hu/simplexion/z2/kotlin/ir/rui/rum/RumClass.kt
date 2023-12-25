/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.rum

import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.air.AirClass
import hu.simplexion.z2.kotlin.ir.rui.ir2rum.BoundaryVisitor
import hu.simplexion.z2.kotlin.ir.rui.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.rum2air.RumClass2Air
import hu.simplexion.z2.kotlin.ir.rui.toRuiClassFqName
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.statements

class RumClass(
    val ruiContext: RuiPluginContext,
    val originalFunction: IrFunction,
    override val parentScope: RumScope?,
) : RumElement, RumScope {

    val fqName = originalFunction.toRuiClassFqName(ruiContext, parentScope != null)
    val name = fqName.shortName()

    var compilationError: Boolean = false

    val boundary: RumBoundary = BoundaryVisitor(ruiContext).findBoundary(originalFunction)

    val originalStatements = checkNotNull(originalFunction.body?.statements) { "missing function body" }

    val initializerStatements = mutableListOf<IrStatement>()
    val renderingStatements = mutableListOf<IrStatement>()

    override val stateVariables = mutableListOf<RumStateVariable>()
    val dirtyMasks = mutableListOf<RumDirtyMask>()

    lateinit var rendering: RumRenderingStatement

    fun toAir(context: RuiPluginContext): AirClass = RumClass2Air(context, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) {
        stateVariables.forEach { it.accept(visitor, data) }
        dirtyMasks.forEach { it.accept(visitor, data) }
        rendering.accept(visitor, data)
    }
}