/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

class ArmExternalStateVariable(
    override val armClass: ArmClass,
    override val indexInState: Int,
    override val indexInClosure: Int,
    override val name: String,
    override val originalType : IrType,
    val symbol : IrSymbol
) : ArmStateVariable {

    override fun matches(symbol: IrSymbol): Boolean = (symbol == this.symbol)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitExternalStateVariable(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

    override fun toString(): String {
        return "EXTERNAL_STATE_VARIABLE indexInState:$indexInState indexInClosure:${indexInClosure} name:$name"
    }

}