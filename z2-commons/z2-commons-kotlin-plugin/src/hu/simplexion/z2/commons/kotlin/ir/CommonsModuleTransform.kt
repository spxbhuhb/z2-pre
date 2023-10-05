/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin.ir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass

class CommonsModuleTransform(
    private val pluginContext: CommonsPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

//        if (declaration.superTypes.contains(pluginContext.commonsType)) {
//            ConsumerClassBuilder(pluginContext, declaration).build()
//            return declaration
//        }
//
//        if (declaration.superTypes.map { it.classFqName }.contains(pluginContext.commonsImplType.classFqName)) {
//            return declaration.accept(ImplClassTransform(pluginContext), null) as IrStatement
//        }

        return declaration
    }

}
