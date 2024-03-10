/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.rum

import hu.simplexion.z2.kotlin.adaptive.ir.rum.visitors.RumElementVisitor
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression

/**
 * @param rumClass The class that belongs to the parameter function.
 */
class RumHigherOrderArgument(
    rumClass: RumClass,
    val index: Int,
    val value: IrFunctionExpression,
    dependencies: RumDependencies,
) : RumExpression(rumClass, value, RumExpressionOrigin.HIGHER_ORDER_ARGUMENT, dependencies) {

    /**
     * Parameters of the parameter function, these are state variables of the anonymous
     * component (in addition to the state variables of the start and intermediate scopes).
     */
    val valueParameters = value.function.valueParameters

    override fun <R, D> accept(visitor: RumElementVisitor<R, D>, data: D): R =
        visitor.visitHigherOrderArgument(this, data)

    override fun <D> acceptChildren(visitor: RumElementVisitor<Unit, D>, data: D) = Unit
}