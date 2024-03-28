/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.util

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import org.jetbrains.kotlin.backend.jvm.codegen.isExtensionFunctionType
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.isKClass
import org.jetbrains.kotlin.types.checker.SimpleClassicTypeSystemContext.getArgument

interface AdaptiveNonAnnotationBasedExtension {

    fun IrValueDeclaration.isAdaptive(adaptiveContext: AdaptivePluginContext) =
        type.isAdaptive()

    fun IrValueParameter.isAdaptive(): Boolean =
        type.isAdaptive()

    fun IrType.isAdaptive() : Boolean {
        if (!isExtensionFunctionType) return false
        val receiver = getArgument(0)
        if (receiver !is IrType) return false
        if (!receiver.isKClass()) return false
        return (receiver.classFqName == FqNames.ADAPTIVE_NAMESPACE)
    }

    fun IrCall.isAdaptive(adaptiveContext: AdaptivePluginContext): Boolean =
        // FIXME recognition of adaptive function calls
        (symbol.owner.extensionReceiverParameter?.let { it.type == adaptiveContext.adaptiveNamespaceClass.defaultType } ?: false)

}

// interface AdaptiveAnnotationBasedExtension : AnnotationBasedExtension {
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
//
//    fun IrValueParameter.isAnnotatedWithAdaptive(): Boolean =
//        toIrBasedDescriptor().hasSpecialAnnotation(null)
//
//    fun IrFunction.isAnnotatedWithAdaptive(): Boolean =
//        toIrBasedDescriptor().hasSpecialAnnotation(null)
//
//    fun IrCall.isAnnotatedWithAdaptive(): Boolean =
//        dispatchReceiver.let { dr ->
//            dr is IrGetValue && dr.symbol.owner.annotations.any { it.type.asString() == Strings.ADAPTIVE_ANNOTATION }
//        } || (symbol.owner.origin == IrDeclarationOrigin.DEFINED && symbol.owner.isAnnotatedWithAdaptive())
//
// }