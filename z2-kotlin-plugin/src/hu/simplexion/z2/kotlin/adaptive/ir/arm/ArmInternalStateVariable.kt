/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2air.ArmInternalStateVariable2Air
import hu.simplexion.z2.kotlin.adaptive.ir.diagnostics.ErrorsAdaptive
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.symbols.IrSymbol

class ArmInternalStateVariable(
    override val armClass: ArmClass,
    override val indexInState: Int,
    override val indexInClosure: Int,
    val irVariable: IrVariable,
) : ArmStateVariable {

    override val name = irVariable.name.identifier

    override fun matches(symbol: IrSymbol): Boolean = (symbol == irVariable.symbol)

    override fun toAir(parent: ClassBoundIrBuilder) = ArmInternalStateVariable2Air(parent, this).toAir()

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitInternalStateVariable(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

    init {
        ErrorsAdaptive.ADAPTIVE_IR_TOO_MANY_STATE_VARIABLES.check(
            armClass,
            irVariable
        ) { indexInClosure <= ADAPTIVE_STATE_VARIABLE_LIMIT }
    }

    override fun toString(): String {
        return "INTERNAL_STATE_VARIABLE indexInState:$indexInState indexInClosure:$indexInClosure name:$name"
    }
}