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
 * Transform the `schematicSchema` property so that it retrieves the value from the companion.
 *
 * ```text
 *     PROPERTY FAKE_OVERRIDE name:schematicSchema visibility:public modality:OPEN [fake_override,val]
 *       overridden:
 *         public open schematicSchema: hu.simplexion.z2.schematic.runtime.schema.Schema [val]
 *       FUN FAKE_OVERRIDE name:<get-schematicSchema> visibility:public modality:OPEN <> ($this:hu.simplexion.z2.schematic.runtime.Schematic<T of hu.simplexion.z2.schematic.runtime.Schematic>) returnType:hu.simplexion.z2.schematic.runtime.schema.Schema [fake_override]
 *         correspondingProperty: PROPERTY FAKE_OVERRIDE name:schematicSchema visibility:public modality:OPEN [fake_override,val]
 *         overridden:
 *           public open fun <get-schematicSchema> (): hu.simplexion.z2.schematic.runtime.schema.Schema declared in hu.simplexion.z2.schematic.runtime.Schematic
 *         $this: VALUE_PARAMETER name:<this> type:hu.simplexion.z2.schematic.runtime.Schematic<T of hu.simplexion.z2.schematic.runtime.Schematic>
 * ```
 */
class SchematicSchemaPropertyTransform(
    override val pluginContext: SchematicPluginContext,
    val classTransform: SchematicClassTransform,
) : IrElementTransformerVoidWithContext(), IrBuilder {

    lateinit var property: IrProperty
    lateinit var type: IrType

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        property = declaration

        if (!property.isFakeOverride) return declaration

        check(!property.isVar) { "schematicSchema must be immutable" }

        property.isFakeOverride = false
        property.origin = IrDeclarationOrigin.DEFINED

        super.visitPropertyNew(property)

        return property
    }

    /**
     * ```text
     * RETURN type=kotlin.Nothing from='public open fun <get-schematicSchema> (): hu.simplexion.z2.schematic.runtime.schema.Schema declared in foo.bar.Adhoc'
     *   CALL 'public final fun <get-schematicSchema> (): hu.simplexion.z2.schematic.runtime.schema.Schema declared in foo.bar.Adhoc.Companion' type=hu.simplexion.z2.schematic.runtime.schema.Schema origin=GET_PROPERTY
     *     $this: GET_OBJECT 'CLASS OBJECT name:Companion modality:FINAL visibility:public [companion] superTypes:[kotlin.Any]' type=foo.bar.Adhoc.Companion
     * ```
     */
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        check(declaration is IrSimpleFunction)
        val funName = declaration.name.asString()
        check("get-" in funName) { "unexpected property function: $funName" }

        declaration.origin = IrDeclarationOrigin.GENERATED_SETTER_GETTER
        declaration.isFakeOverride = false

        declaration.body = DeclarationIrBuilder(irContext, declaration.symbol).irBlockBody {

            +irReturn(
                irCall(
                    classTransform.companionTransform.companionSchematicSchemaGetter,
                    dispatchReceiver = irGetObject(classTransform.companionTransform.companionClass.symbol)
                )
            )
        }

        return declaration
    }

}
