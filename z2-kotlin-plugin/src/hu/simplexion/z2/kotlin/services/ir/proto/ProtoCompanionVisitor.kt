/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.proto

import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid

/**
 * Collect proto companions from the given module.
 */
class ProtoCompanionVisitor(
    val pluginContext: ServicesPluginContext,
    val protoCache: ProtoCache
) : IrElementVisitorVoid {

    override fun visitClass(declaration: IrClass) {
        if (!declaration.isCompanion) return

        val parent = declaration.parent
        if (parent !is IrClass) return

        protoCache.add(parent.defaultType, declaration)
    }

}

