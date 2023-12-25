/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.ir2rum

import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.rum.RumScope
import hu.simplexion.z2.kotlin.ir.rui.rum.RumStateVariable
import hu.simplexion.z2.kotlin.ir.rui.util.RuiAnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner

class DependencyVisitor(
    private val ruiContext : RuiPluginContext,
    private val endScope: RumScope
) : RuiAnnotationBasedExtension, IrElementVisitorVoid {

    var dependencies = mutableListOf<RumStateVariable>()

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    /**
     * Call to the getter.
     */
    override fun visitCall(expression: IrCall) {
        var scope : RumScope? = endScope
        while (scope != null) {
            scope.stateVariables.firstOrNull { it.matches(expression.symbol) }?.let {
                dependencies += it
            }
            scope = scope.parentScope
        }
        super.visitCall(expression)
    }

    /**
     * Parameter get from the original function.
     */
    override fun visitGetValue(expression: IrGetValue) {
        var scope : RumScope? = endScope
        while (scope != null) {
            scope.stateVariables.firstOrNull { it.matches(expression.symbol) }?.let {
                dependencies += it
            }
            scope = scope.parentScope
        }
        super.visitGetValue(expression)
    }
}