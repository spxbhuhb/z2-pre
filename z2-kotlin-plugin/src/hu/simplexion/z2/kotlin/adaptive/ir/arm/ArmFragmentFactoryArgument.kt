/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmFragmentFactoryArgumentBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.name.Name

class ArmFragmentFactoryArgument(
    armClass: ArmClass,
    argumentIndex: Int,
    val argumentName: Name,
    val fragmentIndex: Int,
    val closure: ArmClosure,
    value: IrExpression,
    dependencies: ArmDependencies
) : ArmValueArgument(
    armClass,
    argumentIndex,
    value,
    dependencies,
    origin = ArmExpressionOrigin.FRAGMENT_FACTORY_ARGUMENT
) {

    override fun toPatchExpression(
        classBuilder: ClassBoundIrBuilder,
        patchFun: IrSimpleFunction,
        closure: ArmClosure,
        fragmentParameter: IrValueParameter,
        closureDirtyMask: IrVariable
    ) =
        ArmFragmentFactoryArgumentBuilder(
            classBuilder,
            this,
            closure,
            fragmentParameter,
            closureDirtyMask
        ).genPatchDescendantExpression(patchFun)

    override fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R =
        visitor.visitFragmentFactoryArgument(this, data)

    override fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D) = Unit

}