/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir

import hu.simplexion.z2.kotlin.services.ir.consumer.ConsumerClassTransform
import hu.simplexion.z2.kotlin.services.ir.impl.ImplClassTransform
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.hasAnnotation

class ServicesModuleTransform(
    private val pluginContext: ServicesPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

        // skip classes annotated with @NoServiceTransform
        if (declaration.hasAnnotation(pluginContext.noTransformAnnotation)) {
            return declaration
        }

        // create consumer classes if `Service` in the supertypes
        if (declaration.superTypes.contains(pluginContext.serviceType)) {
            ConsumerClassTransform(pluginContext, declaration).build()
            return declaration
        }

        // transform implementations
        if (declaration.superTypes.map { it.classFqName }.contains(pluginContext.serviceImplType.classFqName)) {
            return declaration.accept(ImplClassTransform(pluginContext), null) as IrStatement
        }

        return declaration
    }

}
