/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.css

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.isTopLevelDeclaration
import org.jetbrains.kotlin.ir.util.isVararg
import org.jetbrains.kotlin.ir.util.primaryConstructor

class CssModuleTransform(
    private val pluginContext: CssPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitCall(expression: IrCall): IrExpression {
        val func = expression.symbol.owner

        // TODO proper selection of optimized functions
        if ("replace" in func.name.asString() || "remove" in func.name.asString()) return super.visitCall(expression)
        if (func.valueParameters.isEmpty()) return super.visitCall(expression)

        val firstParam = func.valueParameters.first()
        if (!firstParam.isVararg || firstParam.varargElementType?.isSubtypeOfClass(pluginContext.cssClass) != true) return super.visitCall(expression)

        val firstArgument = expression.getValueArgument(0)
        if (firstArgument !is IrVararg) return super.visitCall(expression)

        val result = mutableListOf<IrVarargElement>()
        val optimized = mutableListOf<String>()

        for (arg in firstArgument.elements) {
            if (arg !is IrCall) {
                result += arg
                continue
            }
            if (arg.origin != IrStatementOrigin.GET_PROPERTY) {
                result += arg
                continue
            }

            val getter = arg.symbol.owner
            if (getter.extensionReceiverParameter != null) { // val String.css get() = CssClass(this)
                result += arg
                continue
            }

            val property = getter.correspondingPropertySymbol?.owner

            if (property?.isTopLevelDeclaration != true) {
                result += arg
                continue
            }

            optimized += property.name.identifier.toSnakeCase()
        }

        if (optimized.isNotEmpty()) {
            result += IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                pluginContext.cssClass.defaultType,
                pluginContext.cssClass.owner.primaryConstructor!!.symbol,
                0, 0,
                1 // name
            ).also { constructorCall ->
                constructorCall.putValueArgument(
                    0,
                    IrConstImpl(
                        UNDEFINED_OFFSET,
                        UNDEFINED_OFFSET,
                        pluginContext.irContext.irBuiltIns.stringType,
                        IrConstKind.String,
                        optimized.joinToString(" ")
                    )
                )
            }
        }

        firstArgument.elements.clear()
        firstArgument.elements.addAll(result)

        return super.visitCall(expression)
    }

    fun String.toSnakeCase(): String {
        val out = mutableListOf<Char>()
        var inNumber = false
        for (char in toCharArray()) {

            when {
                char.isUpperCase() -> {
                    out += '-'
                    out += char.lowercaseChar()
                    inNumber = false
                }

                char.isDigit() -> {
                    if (!inNumber) out += '-'
                    out += char
                    inNumber = true
                }

                else -> {
                    out += char
                    inNumber = false
                }
            }
        }
        return out.toCharArray().concatToString()
    }


}
