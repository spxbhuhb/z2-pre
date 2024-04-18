/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir.store

import hu.simplexion.z2.kotlin.schematic.Names
import hu.simplexion.z2.kotlin.schematic.ir.SchematicPluginContext
import hu.simplexion.z2.kotlin.schematic.ir.util.IrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty

/**
 * Transforms an interface that implements `SchematicEntityStore`:
 *
 * - set the getter of `schematicEntityCompanion`
 */
class SchematicEntityStoreTransform(
    override val pluginContext: SchematicPluginContext
) : IrElementTransformerVoidWithContext(), IrBuilder {

    lateinit var transformedClass: IrClass

    override fun visitClassNew(declaration: IrClass): IrStatement {
        transformedClass = declaration
        return super.visitClassNew(declaration)
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (declaration.name != Names.SCHEMATIC_ENTITY_COMPANION_PROPERTY) return declaration

        check(!declaration.isVar) { "schematicEntityCompanion must be immutable" }

        declaration.backingField = null

        declaration.accept(SchematicEntityCompanionProperty(pluginContext, this), null)

        return declaration
    }

}
