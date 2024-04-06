/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.util

import hu.simplexion.z2.kotlin.adaptive.Names
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import org.jetbrains.kotlin.backend.jvm.codegen.isExtensionFunctionType
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass

interface AdaptiveNonAnnotationBasedExtension {

    fun IrValueDeclaration.isAdaptive(adaptiveContext: AdaptivePluginContext) =
        type.isAdaptive(adaptiveContext)

    fun IrValueParameter.isAdaptive(adaptiveContext: AdaptivePluginContext): Boolean =
        type.isAdaptive(adaptiveContext)

    fun IrType.isAdaptive(adaptiveContext: AdaptivePluginContext): Boolean {
        if (!isExtensionFunctionType) return false
        if (this !is IrSimpleTypeImpl) return false
        val receiver = arguments[0]
        if (receiver !is IrType) return false
        return receiver.isSubtypeOfClass(adaptiveContext.adaptiveNamespaceClass)
    }

    fun IrCall.isDirectAdaptiveCall(adaptiveContext: AdaptivePluginContext): Boolean =
        (symbol.owner.extensionReceiverParameter?.let { it.type == adaptiveContext.adaptiveNamespaceClass.defaultType }
            ?: false)

    fun IrCall.isArgumentAdaptiveCall(adaptiveContext: AdaptivePluginContext): Boolean =
        symbol.owner.name == Names.KOTLIN_INVOKE && dispatchReceiver!!.type.isAdaptive(adaptiveContext) // TODO better check for kotlin invoke
}