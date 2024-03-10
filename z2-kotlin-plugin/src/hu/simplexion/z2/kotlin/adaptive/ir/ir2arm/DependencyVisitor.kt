/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.ir2arm

import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner

/**
 * Collects state variable dependencies. These may be:
 *
 * - [IrCall]: variable getter
 * - [IrGetValue]: access a parameter of the original function
 *
 */
class DependencyVisitor(
    private val adaptiveContext : AdaptivePluginContext,
    private val endScope: ArmClass
) : AdaptiveAnnotationBasedExtension, IrElementVisitorVoid {

    var dependencies = mutableListOf<ArmStateVariable>()

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        listOf(Strings.ADAPTIVE_ANNOTATION)

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    /**
     * Call to the getter.
     */
    override fun visitCall(expression: IrCall) {
        var scope : ArmClass? = endScope
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
        var scope : ArmClass? = endScope
        while (scope != null) {
            scope.stateVariables.firstOrNull { it.matches(expression.symbol) }?.let {
                dependencies += it
            }
            scope = scope.parentScope
        }
        super.visitGetValue(expression)
    }
}