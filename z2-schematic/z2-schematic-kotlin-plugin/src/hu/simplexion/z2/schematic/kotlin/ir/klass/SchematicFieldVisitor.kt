/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.klass

import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import hu.simplexion.z2.schematic.kotlin.ir.util.IrBuilder
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.backend.js.utils.typeArguments
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

/**
 * Analyze the FDF call and the possible DEF calls.
 * Build the SchemaField constructor call.
 */
class SchematicFieldVisitor(
    override val pluginContext: SchematicPluginContext,
) : IrElementVisitorVoid, IrBuilder {

    lateinit var property: IrProperty
    lateinit var type: IrType
    lateinit var schemaField: IrVarargElement

    val codePoint
        get() = "${property.parent.kotlinFqName}.${property.name}"

    override fun visitProperty(declaration: IrProperty) {

        property = declaration

        val backingField = checkNotNull(declaration.backingField) { "missing backing field: $codePoint" }

        // this is the call to provideDelegate
        // it's first argument is the call to the type function (`int`, `string` etc.)
        // these calls must return with a SchemaField instance which will be passed to the schema

        var fieldBuildCall = checkNotNull(backingField.initializer?.expression) { "missing backing field expression: $codePoint" }
        check(fieldBuildCall is IrCall) { "backing field expression is not a call: $codePoint" }

        fieldBuildCall = setCompanionWhenSchematic(fieldBuildCall)

        schemaField = irCall(
            pluginContext.schemaFieldSetName,
            dispatchReceiver = fieldBuildCall
        ).also {
            it.putValueArgument(0, irConst(property.name.identifier))
        }
    }

    private fun setCompanionWhenSchematic(fieldBuildCall: IrCall): IrExpression {
        val schemaFieldType = fieldBuildCall.type
        check(schemaFieldType.isSubtypeOfClass(pluginContext.schemaFieldClass)) { "delegate does not implement SchemaField: $codePoint" }

        if (! schemaFieldType.isSubtypeOfClass(pluginContext.schematicSchemaFieldClass)) {
            return fieldBuildCall
        }

        val companion = fieldBuildCall.typeArguments
            .filterNotNull()
            .filter { it.isSubtypeOfClass(pluginContext.schematicClass) }
            .map { it.getCompanion() }
            .single()

        return irCall(
            pluginContext.schematicSchemaFieldSetCompanion,
            dispatchReceiver = fieldBuildCall
        ).also {
            it.putValueArgument(0, irGetObject(companion.symbol))
        }
    }

    private fun IrType.getCompanion(): IrClass {
        val companion = getClass()?.companionObject()
        if (companion != null) return companion

        val typeFqName = checkNotNull(classFqName) { "cannot load companion for ${this.asString()}" }
        val classId = ClassId(typeFqName.parent(), Name.identifier(typeFqName.shortName().identifier + "\$Companion")) //.createNestedClassId(Name.identifier(COMPANION_OBJECT_NAME))

        return checkNotNull(pluginContext.irContext.referenceClass(classId)) { "cannot load companion for ${classId.asString()}" }.owner
    }
}

