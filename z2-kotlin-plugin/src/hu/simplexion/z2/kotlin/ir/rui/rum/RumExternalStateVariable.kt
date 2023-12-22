/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.rum

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.RUI_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.ir.rui.diagnostics.ErrorsRui.RUI_IR_TOO_MANY_STATE_VARIABLES
import hu.simplexion.z2.kotlin.ir.rui.rum.visitors.RumElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.rum2air.RumExternalStateVariable2Air
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.symbols.IrSymbol

class RumExternalStateVariable(
    override val rumClass: RumClass,
    override val index: Int,
    val irValueParameter: IrValueParameter
) : RumStateVariable {

    override val originalName = irValueParameter.name.identifier
    override val name = irValueParameter.name

    override fun matches(symbol: IrSymbol): Boolean = (symbol == irValueParameter.symbol)

    override fun toAir(parent: ClassBoundIrBuilder) = RumExternalStateVariable2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitExternalStateVariable(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) = Unit

    init {
        RUI_IR_TOO_MANY_STATE_VARIABLES.check(rumClass, irValueParameter) { index <= RUI_STATE_VARIABLE_LIMIT }
    }

}