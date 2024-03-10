/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.util

import hu.simplexion.z2.kotlin.ir.adaptive.ADAPTIVE_ANNOTATION
import org.jetbrains.kotlin.extensions.AnnotationBasedExtension
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue

interface AdaptiveAnnotationBasedExtension : AnnotationBasedExtension {

//    fun isAdaptive(declaration: IrClass): Boolean =
//        declaration.kind == ClassKind.CLASS &&
//            declaration.isAnnotatedWithAdaptive()
//
//    fun isAdaptive(declaration: IrFunction): Boolean =
//        declaration.isAnnotatedWithAdaptive()
//
//    fun isAdaptive(symbol: IrSimpleFunctionSymbol): Boolean =
//        symbol.owner.isAnnotatedWithAdaptive()
//
//    fun IrClass.isAnnotatedWithAdaptive(): Boolean =
//        toIrBasedDescriptor().hasSpecialAnnotation(null)

    fun IrValueParameter.isAnnotatedWithAdaptive(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

    fun IrFunction.isAnnotatedWithAdaptive(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

    fun IrCall.isAnnotatedWithAdaptive(): Boolean =
        dispatchReceiver.let { dr ->
            dr is IrGetValue && dr.symbol.owner.annotations.any { it.type.asString() == ADAPTIVE_ANNOTATION }
        } || (symbol.owner.origin == IrDeclarationOrigin.DEFINED && symbol.owner.isAnnotatedWithAdaptive())

}