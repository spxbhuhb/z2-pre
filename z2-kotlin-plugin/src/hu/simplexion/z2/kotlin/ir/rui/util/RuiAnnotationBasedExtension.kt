/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui.util

import hu.simplexion.z2.kotlin.ir.rui.RUI_ANNOTATION
import org.jetbrains.kotlin.extensions.AnnotationBasedExtension
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue

interface RuiAnnotationBasedExtension : AnnotationBasedExtension {

//    fun isRui(declaration: IrClass): Boolean =
//        declaration.kind == ClassKind.CLASS &&
//            declaration.isAnnotatedWithRui()
//
//    fun isRui(declaration: IrFunction): Boolean =
//        declaration.isAnnotatedWithRui()
//
//    fun isRui(symbol: IrSimpleFunctionSymbol): Boolean =
//        symbol.owner.isAnnotatedWithRui()
//
//    fun IrClass.isAnnotatedWithRui(): Boolean =
//        toIrBasedDescriptor().hasSpecialAnnotation(null)

    fun IrValueParameter.isAnnotatedWithRui(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

    fun IrFunction.isAnnotatedWithRui(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

    fun IrCall.isAnnotatedWithRui(): Boolean =
        dispatchReceiver.let { dr ->
            dr is IrGetValue && dr.symbol.owner.annotations.any { it.type.asString() == RUI_ANNOTATION }
        } || (symbol.owner.origin == IrDeclarationOrigin.DEFINED && symbol.owner.isAnnotatedWithRui())

}