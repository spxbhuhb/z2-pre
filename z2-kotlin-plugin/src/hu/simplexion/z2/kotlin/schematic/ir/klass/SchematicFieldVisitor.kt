/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir.klass

import hu.simplexion.z2.kotlin.schematic.ir.SchematicPluginContext
import hu.simplexion.z2.kotlin.schematic.ir.util.IrBuilder
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.backend.js.utils.typeArguments
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
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

        // replace the field builder parameter if the field is a ListSchemaField

        // replace the field builder call with one that sets the companion of the referenced class
        // FROM:  val a by schematic<A>()
        // TO:    val a by schematic<A>().setCompanion(A.Companion)

        fieldBuildCall = setCompanionWhenSchematic(fieldBuildCall)

        // replace the field builder call with one that sets the field name:
        // FROM:  val a by string()
        // TO:    val a by string().setFieldName("a")

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

        return with (pluginContext) {
            when {
                schemaFieldType.isSubtypeOfClass(referenceSchemaFieldClass) -> setCompanion(fieldBuildCall, referenceSchemaFieldSetCompanion)
                schemaFieldType.isSubtypeOfClass(nullableReferenceSchemaFieldClass) -> setCompanion(fieldBuildCall, nullableReferenceSchemaFieldSetCompanion)
                schemaFieldType.isSubtypeOfClass(referenceListSchemaFieldClass) -> setCompanion(fieldBuildCall, referenceListSchemaFieldClassSetCompanion)

                schemaFieldType.isSubtypeOfClass(schematicSchemaFieldClass) -> setCompanion(fieldBuildCall, schematicSchemaFieldSetCompanion)
                schemaFieldType.isSubtypeOfClass(nullableSchematicSchemaFieldClass) -> setCompanion(fieldBuildCall, nullableSchematicSchemaFieldSetCompanion)
                schemaFieldType.isSubtypeOfClass(schematicListSchemaFieldClass) -> setCompanion(fieldBuildCall, schematicListSchemaFieldClassSetCompanion)
                else -> fieldBuildCall
            }
        }
    }

    fun setCompanion(fieldBuildCall: IrCall, setCompanionFun : IrSimpleFunctionSymbol) : IrExpression {
        val companion = getTypeArguments(fieldBuildCall)
            .filterNotNull()
            .filter { it.isSubtypeOfClass(pluginContext.schematicClass) }
            .map { it.getCompanion() }
            .single()

        return irCall(
            setCompanionFun,
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

    fun getTypeArguments(call : IrCall) : List<IrType?> {
        var current = call
        while (current.typeArguments.isEmpty()) {
            val receiver = current.dispatchReceiver
            if (receiver is IrCall) {
                current = receiver
            } else {
                return emptyList()
            }
        }
        return current.typeArguments
    }
}

