/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.rum

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.RUI_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.ir.rui.diagnostics.ErrorsRui
import hu.simplexion.z2.kotlin.ir.rui.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.rum2air.RumInternalStateVariable2Air
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.symbols.IrSymbol

class RumInternalStateVariable(
    override val rumClass: RumClass,
    override val index: Int,
    val irVariable: IrVariable,
) : RumStateVariable {

    override val originalName = irVariable.name.identifier
    override val name = irVariable.name

    override fun matches(symbol: IrSymbol): Boolean = (symbol == irVariable.symbol)

    override fun toAir(parent: ClassBoundIrBuilder) = RumInternalStateVariable2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitInternalStateVariable(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) = Unit

    init {
        ErrorsRui.RUI_IR_TOO_MANY_STATE_VARIABLES.check(rumClass, irVariable) { index <= RUI_STATE_VARIABLE_LIMIT }
    }

}