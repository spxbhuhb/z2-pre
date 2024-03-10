/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import org.jetbrains.kotlin.ir.declarations.IrDeclaration

class ArmDeclaration(
    val armClass: ArmClass,
    val irDeclaration: IrDeclaration,
    val origin: ArmDeclarationOrigin,
    val dependencies: ArmDependencies
) : ArmElement {

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitDeclaration(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

}