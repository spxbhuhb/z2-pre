/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmClass2Air
import hu.simplexion.z2.kotlin.adaptive.ir.ir2arm.BoundaryVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.util.adaptiveClassFqName
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.statements

class ArmClass(
    val adaptiveContext: AdaptivePluginContext,
    val originalFunction: IrFunction
) : ArmElement {

    val fqName = originalFunction.adaptiveClassFqName()
    val name = fqName.shortName()

    var compilationError: Boolean = false

    val boundary: ArmBoundary = BoundaryVisitor(adaptiveContext).findBoundary(originalFunction)

    val originalStatements = checkNotNull(originalFunction.body?.statements) { "missing function body" }

    val stateDefinitionStatements = mutableListOf<ArmStateDefinitionStatement>()
    val originalRenderingStatements = mutableListOf<IrStatement>()

    val stateVariables = mutableListOf<ArmStateVariable>()

    val rendering = mutableListOf<ArmRenderingStatement>()

    fun toAir(context: AdaptivePluginContext): AirClass = ArmClass2Air(context, this).toAir()

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) {
        stateVariables.filterIsInstance<ArmExternalStateVariable>().forEach { it.accept(visitor, data) }
        stateDefinitionStatements.forEach { it.accept(visitor, data) }
        rendering.forEach { it.accept(visitor, data) }
    }
}