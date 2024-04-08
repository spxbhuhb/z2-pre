/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmAccessBindingArgumentBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression

/**
 * A function argument that is an AdaptiveAccessBinding.
 */
class ArmAccessBindingArgument(
    armClass: ArmClass,
    argumentIndex: Int,
    val indexInState: Int,
    val indexInClosure: Int,
    value: IrFunctionExpression,
    dependencies: ArmDependencies,
) : ArmValueArgument(armClass, argumentIndex, value, dependencies) {

    override fun toPatchExpression(
        classBuilder: ClassBoundIrBuilder,
        patchFun : IrSimpleFunction,
        closure: ArmClosure,
        fragmentParameter: IrValueParameter,
        closureDirtyMask: IrVariable
    ) =
        ArmAccessBindingArgumentBuilder(
            classBuilder,
            this,
            closure,
            fragmentParameter,
            closureDirtyMask
        ).genPatchDescendantExpression(patchFun)

}