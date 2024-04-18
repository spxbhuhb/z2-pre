/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir.companion

import hu.simplexion.z2.kotlin.schematic.Names
import hu.simplexion.z2.kotlin.schematic.ir.SchematicPluginContext
import hu.simplexion.z2.kotlin.schematic.ir.util.IrBuilder
import hu.simplexion.z2.kotlin.util.property
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.primaryConstructor

/**
 * Transform the `schematicSchema` property of the companion.
 */
class SchematicSchemaProperty(
    override val pluginContext: SchematicPluginContext,
    val companionTransform: CompanionTransform,
) : IrBuilder {

    val companionClass = companionTransform.companionClass

    val property = companionClass.property(Names.SCHEMATIC_SCHEMA_PROPERTY)

    fun build() {
        companionTransform.companionSchematicSchemaGetter = checkNotNull(property.getter?.symbol)

        check(!property.isVar) { "schematicSchema must be immutable" }

        property.backingField!!.initializer = irFactory.createExpressionBody(buildSchemaInitializer())
    }

    fun buildSchemaInitializer(): IrExpression =

        IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            pluginContext.schemaClass.defaultType,
            pluginContext.schemaClass.owner.primaryConstructor!!.symbol,
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
