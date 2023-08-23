/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.companion

import hu.simplexion.z2.schematic.kotlin.ir.SCHEMATIC_COMPANION_CLASS
import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import hu.simplexion.z2.schematic.kotlin.ir.klass.SchematicClassTransform
import hu.simplexion.z2.schematic.kotlin.ir.util.IrBuilder
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.expressions.addElement
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.defaultType

class CompanionTransform(
    override val pluginContext: SchematicPluginContext,
    val classTransform: SchematicClassTransform,
) : IrBuilder {

    val transformedClass: IrClass
        get() = classTransform.transformedClass

    lateinit var companionClass: IrClass
    lateinit var companionSchematicSchemaGetter: IrFunctionSymbol

    /**
     * Arguments for the schema that contains the fields.
     * Initialized by [SchematicSchemaProperty].
     */
    lateinit var schemaFieldsArg: IrVarargImpl

    // index of the field in `Schema.fields`
    var fieldIndex = 0

    fun addOrGetCompanionClass() {
        companionClass = transformedClass.addCompanionIfMissing()

        // add the SchematicCompanion interface if it is missing
        if (companionClass.superTypes.firstOrNull { it.classFqName?.shortName()?.identifier == SCHEMATIC_COMPANION_CLASS } == null) {
            companionClass.superTypes += listOf(pluginContext.schematicCompanionClass.typeWith(transformedClass.defaultType))
        }

        SchematicSchemaProperty(pluginContext, this).build()
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
