/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.schematic.companion

import hu.simplexion.z2.kotlin.ir.schematic.SCHEMATIC_FQNAME_PROPERTY
import hu.simplexion.z2.kotlin.ir.schematic.SchematicPluginContext
import hu.simplexion.z2.kotlin.ir.schematic.util.IrBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.Name

/**
 * Transform the `schematicFqName` property of the companion. The property
 * may be:
 *
 * - missing, when the companion class is created by the plugin - add
 * - fake override, when the companion is declared but the property is not overridden - convert
 * - override, when there is an actual implementation - do not touch
 */
class SchematicFqNameProperty(
    override val pluginContext: SchematicPluginContext,
    val companionTransform: CompanionTransform,
) : IrBuilder {

    val companionClass = companionTransform.companionClass

    lateinit var property: IrProperty

    fun build() {
        val existing = companionClass.properties.firstOrNull { it.name.identifier == SCHEMATIC_FQNAME_PROPERTY }

        when {
            existing == null -> add()
            existing.origin is IrDeclarationOrigin.GeneratedByPlugin -> transformExisting(existing, existing.origin)
            existing.isFakeOverride -> transformExisting(existing, null)
            else -> property = existing
        }

        companionTransform.companionSchematicFqNameGetter = checkNotNull(property.getter?.symbol)
    }

    fun add() {
        property = companionClass.addIrProperty(
            Name.identifier(SCHEMATIC_FQNAME_PROPERTY),
            pluginContext.irContext.irBuiltIns.stringType,
            inIsVar = false,
            buildFqNameExpression(),
            listOf(pluginContext.schematicCompanionSchematicSchema)
        ).also {
            it.modality = Modality.FINAL
        }
    }

    fun transformExisting(declaration: IrProperty, generatedOrigin: IrDeclarationOrigin?) {
        property = declaration

        check(! property.isVar) { "schematicFqName must be immutable" }

        property.isFakeOverride = false
        property.origin = generatedOrigin ?: IrDeclarationOrigin.DEFINED

        val irField = irFactory.buildField {
            name = Name.identifier(SCHEMATIC_FQNAME_PROPERTY)
            type = pluginContext.irContext.irBuiltIns.stringType
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = companionClass
            initializer = irFactory.createExpressionBody(buildFqNameExpression())
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

    fun buildFqNameExpression(): IrExpression =
        irConst(companionTransform.classTransform.transformedClass.kotlinFqName.asString())

}
