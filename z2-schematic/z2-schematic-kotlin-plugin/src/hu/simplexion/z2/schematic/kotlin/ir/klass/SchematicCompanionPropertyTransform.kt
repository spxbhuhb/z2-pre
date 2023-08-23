/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.klass

import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import hu.simplexion.z2.schematic.kotlin.ir.util.IrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.types.IrType

/**
 * Transform the `schematicCompanion` property so that it retrieves the companion.
 */
class SchematicCompanionPropertyTransform(
    override val pluginContext: SchematicPluginContext,
    val classTransform: SchematicClassTransform,
) : IrElementTransformerVoidWithContext(), IrBuilder {

    lateinit var property: IrProperty
    lateinit var type: IrType

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        property = declaration

        if (!property.isFakeOverride) return declaration

        check(!property.isVar) { "schematicCompanion must be immutable" }

        property.isFakeOverride = false
        property.origin = IrDeclarationOrigin.DEFINED

        super.visitPropertyNew(property)

        return property
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        check(declaration is IrSimpleFunction)
        val funName = declaration.name.asString()
        check("get-" in funName) { "unexpected property function: $funName" }

        declaration.origin = IrDeclarationOrigin.GENERATED_SETTER_GETTER
        declaration.isFakeOverride = false

        declaration.body = DeclarationIrBuilder(irContext, declaration.symbol).irBlockBody {
            +irReturn(
                irGetObject(classTransform.companionTransform.companionClass.symbol)
            )
        }

        return declaration
    }

}
