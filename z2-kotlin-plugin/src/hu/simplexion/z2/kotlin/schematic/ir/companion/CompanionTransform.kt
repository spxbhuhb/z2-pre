/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir.companion

import hu.simplexion.z2.kotlin.schematic.ir.SchematicPluginContext
import hu.simplexion.z2.kotlin.schematic.ir.klass.SchematicClassTransform
import hu.simplexion.z2.kotlin.schematic.ir.util.IrBuilder
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.expressions.addElement
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.util.companionObject

class CompanionTransform(
    override val pluginContext: SchematicPluginContext,
    val classTransform: SchematicClassTransform,
) : IrBuilder {

    val transformedClass: IrClass
        get() = classTransform.transformedClass

    val companionClass: IrClass
        get() = transformedClass.companionObject() !!

    lateinit var companionSchematicSchemaGetter: IrFunctionSymbol
    lateinit var companionSchematicFqNameGetter: IrFunctionSymbol

    /**
     * Arguments for the schema that contains the fields.
     * Initialized by [SchematicSchemaProperty].
     */
    lateinit var schemaFieldsArg: IrVarargImpl

    // index of the field in `Schema.fields`
    var fieldIndex = 0

    fun addOrGetCompanionClass() {
        SchematicSchemaProperty(pluginContext, this).build()
        SchematicFqNameProperty(pluginContext, this).build()
        SchematicStoreProperty(pluginContext, this).build()
        NewInstance(pluginContext, this).build()
        ProtoEncode(pluginContext, this).build()
        ProtoDecode(pluginContext, this).build()
    }

    fun finish() {
        classTransform.fieldVisitors.forEach { schemaFieldsArg.addElement(it.schemaField) }
        SetFieldValue(pluginContext, this).build()
        GetFieldValue(pluginContext, this).build()
    }

}
