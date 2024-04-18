/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir.store

import hu.simplexion.z2.kotlin.schematic.ClassIds
import hu.simplexion.z2.kotlin.schematic.ir.SchematicPluginContext
import hu.simplexion.z2.kotlin.schematic.ir.util.IrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.typeOrNull
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.companionObject

/**
 * Transform the `schematicEntityCompanion` property of an interface that implements `SchematicEntityStore`.
 */
class SchematicEntityCompanionProperty(
    override val pluginContext: SchematicPluginContext,
    classTransform: SchematicEntityStoreTransform,
) : IrElementTransformerVoidWithContext(), IrBuilder {

    val transformedClass = classTransform.transformedClass

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        check(declaration is IrSimpleFunction)
        val funName = declaration.name.asString()
        check("get-" in funName) { "unexpected property function: $funName" }

        // fake overrides should be kept as they are
        // this happens in the implementations
        if (declaration.isFakeOverride) return declaration

        val supertype = transformedClass.superTypes.first { it.classOrNull?.owner?.classId == ClassIds.SCHEMATIC_ENTITY_STORE }
        check(supertype is IrSimpleType)

        val companion = requireNotNull(supertype.arguments.first().typeOrNull?.classOrNull?.owner?.companionObject())

        // without these the IR backend throw an exception about a missing function BODY
        declaration.origin = IrDeclarationOrigin.GENERATED_SETTER_GETTER
        declaration.visibility = DescriptorVisibilities.PUBLIC
        declaration.modality = Modality.OPEN

        declaration.body = DeclarationIrBuilder(irContext, declaration.symbol).irBlockBody {
            +irReturn(
                irGetObject(companion.symbol)
            )
        }

        return declaration
    }

}
