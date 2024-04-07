/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

/**
 * A fake state variable created for `when(subject)` transformation.
 */
class ArmWhenStateVariable(
    override val armClass: ArmClass,
    override val indexInState: Int,
    override val indexInClosure: Int,
    override val name: String,
    val irVariable: IrVariable
) : ArmStateDefinitionStatement(irVariable, emptyList()), ArmStateVariable {

    override val type: IrType
        get() = irVariable.type

    override fun matches(symbol: IrSymbol): Boolean = (symbol == irVariable.symbol)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitWhenStateVariable(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

    override fun toString(): String {
        return "WHEN_STATE_VARIABLE indexInClosure:$indexInClosure name:$name"
    }
}