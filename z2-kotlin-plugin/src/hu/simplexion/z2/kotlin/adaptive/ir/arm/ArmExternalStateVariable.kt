/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

class ArmExternalStateVariable(
    override val armClass: ArmClass,
    override val indexInState: Int,
    override val indexInClosure: Int,
    val irValueParameter: IrValueParameter
) : ArmStateVariable {

    override val name = irValueParameter.name.identifier

    override val originalType: IrType
        get() = irValueParameter.type

    override fun matches(symbol: IrSymbol): Boolean = (symbol == irValueParameter.symbol)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitExternalStateVariable(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

    override fun toString(): String {
        return "EXTERNAL_STATE_VARIABLE indexInState:$indexInState indexInClosure:${indexInClosure} name:$name"
    }

}