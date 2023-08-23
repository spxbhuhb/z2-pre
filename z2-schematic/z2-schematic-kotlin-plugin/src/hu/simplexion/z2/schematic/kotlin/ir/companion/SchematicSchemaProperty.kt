/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.companion

import hu.simplexion.z2.schematic.kotlin.ir.SCHEMATIC_SCHEMA_PROPERTY
import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import hu.simplexion.z2.schematic.kotlin.ir.util.IrBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.Name

/**
 * Transform the `schematicSchema` property of the companion. The property
 * may be:
 *
 * - missing, when the companion class is created by the plugin - add
 * - fake override, when the companion is declared but the property is not overridden - convert
 * - override, when there is an actual implementation - do not touch
 */
class SchematicSchemaProperty(
    override val pluginContext: SchematicPluginContext,
    val companionTransform: CompanionTransform,
) : IrBuilder {

    val companionClass = companionTransform.companionClass

    lateinit var property: IrProperty

    fun build() {
        val existing = companionClass.properties.firstOrNull { it.name.identifier == SCHEMATIC_SCHEMA_PROPERTY }

        when {
            existing == null -> add()
            existing.isFakeOverride -> transformFake(existing)
            else -> property = existing
        }

        companionTransform.companionSchematicSchemaGetter = checkNotNull(property.getter?.symbol)
    }

    fun add() {
        property = companionClass.addIrProperty(
            Name.identifier(SCHEMATIC_SCHEMA_PROPERTY),
            pluginContext.schemaClass.defaultType,
            inIsVar = false,
            buildSchemaInitializer(),
            listOf(pluginContext.schematicCompanionSchematicSchema)
        ).also {
            it.modality = Modality.FINAL
        }
    }

    fun transformFake(declaration: IrProperty) {
        property = declaration

        check(! property.isVar) { "schematicSchema must be immutable" }

        property.isFakeOverride = false
        property.origin = IrDeclarationOrigin.DEFINED

        val irField = irFactory.buildField {
            name = Name.identifier(SCHEMATIC_SCHEMA_PROPERTY)
            type = pluginContext.schemaClass.defaultType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = companionClass
            initializer = irFactory.createExpressionBody(buildSchemaInitializer())
            correspondingPropertySymbol = property.symbol
        }

        property.backingField = irField

        transformFakeGetter(property.getter !!, irField)
    }

    fun transformFakeGetter(declaration: IrFunction, field: IrField) {
        check(declaration is IrSimpleFunction)

        declaration.origin = IrDeclarationOrigin.GENERATED_SETTER_GETTER
        declaration.isFakeOverride = false

        declaration.body = DeclarationIrBuilder(irContext, declaration.symbol).irBlockBody {
            + irReturn(
                IrGetFieldImpl(
                    UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                    field.symbol,
                    field.type,
                    IrGetValueImpl(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        declaration.dispatchReceiverParameter !!.type,
                        declaration.dispatchReceiverParameter !!.symbol
                    )
                )
            )
        }
    }

    fun buildSchemaInitializer(): IrExpression =

        IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            pluginContext.schemaClass.defaultType,
            pluginContext.schemaClass.owner.primaryConstructor !!.symbol,
            0, 0,
            2 // companion, array of fields
        ).also { constructorCall ->
            constructorCall.putValueArgument(0, irGetObject(companionClass.symbol))
            constructorCall.putValueArgument(1, buildFragmentVarArg())
        }

    fun buildFragmentVarArg(): IrExpression =

        IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.arrayClass.typeWith(pluginContext.schemaFieldType),
            pluginContext.schemaFieldType
        ).also {
            companionTransform.schemaFieldsArg = it
        }

}
