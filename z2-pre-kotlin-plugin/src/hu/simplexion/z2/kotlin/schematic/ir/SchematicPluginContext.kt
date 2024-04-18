/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.schematic.*
import hu.simplexion.z2.kotlin.schematic.ir.util.SchematicFunctionCache
import hu.simplexion.z2.kotlin.util.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.getSimpleFunction

class SchematicPluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    override val runtimePackage = Strings.SCHEMATIC_RUNTIME_PACKAGE

    // TODO globals.kt and inline names here got a bit confused, clear it up
    val schematicClass = Strings.SCHEMATIC_CLASS.runtimeClass()
    val schematicEntityClass = Strings.SCHEMATIC_ENTITY_CLASS.runtimeClass(pkg = Strings.SCHEMATIC_ENTITY_PACKAGE)
    val schematicEntityStoreClass = Strings.SCHEMATIC_ENTITY_STORE_CLASS.runtimeClass(pkg = Strings.SCHEMATIC_ENTITY_PACKAGE)
    val schematicToAccessContext = checkNotNull(schematicClass.getSimpleFunction(SCHEMATIC_TO_ACCESS_CONTEXT)) { "missing Schematic.toSchematicAccessContext" }

    val schemaClass = Strings.SCHEMATIC_SCHEMA_CLASS.runtimeClass(Strings.SCHEMATIC_SCHEMA_PACKAGE)
    val schemaInitWithDefaults = checkNotNull(schemaClass.getSimpleFunction("initWithDefaults")?.owner?.symbol) { "missing Schema.initWithDefault" }
    val schemaGetField = checkNotNull(schemaClass.getSimpleFunction("getField")?.owner?.symbol) { "Schema.getField is missing" }
    val schemaEncodeProto = checkNotNull(schemaClass.functionByName(ENCODE_PROTO).owner.symbol)
    val schemaDecodeProto = checkNotNull(schemaClass.functionByName(DECODE_PROTO).owner.symbol)
    val schemaFieldNotFound = SCHEMA_FIELD_NOT_FOUND_CLASS.runtimeClass(Strings.SCHEMATIC_SCHEMA_PACKAGE).owner.constructors.first().symbol
    val schemaFieldIsImmutable = SCHEMA_FIELD_IS_IMMUTABLE.runtimeClass(Strings.SCHEMATIC_SCHEMA_PACKAGE).owner.constructors.first().symbol

    val safAnnotationConstructor = SCHEMATIC_ACCESS_FUNCTION_CLASS.runtimeClass().owner.constructors.first().symbol

    val schemaFieldClass = SCHEMA_FIELD_CLASS.runtimeClass(Strings.SCHEMATIC_SCHEMA_PACKAGE)
    val schemaFieldType = schemaFieldClass.defaultType
    val schemaFieldSetName = schemaFieldClass.functionByName(SCHEMA_FIELD_SET_FIELD_NAME)

    val referenceSchemaFieldClass = "ReferenceSchemaField".runtimeClass(RUNTIME_SCHEMA_FIELD_PACKAGE)
    val referenceSchemaFieldSetCompanion = referenceSchemaFieldClass.functionByName("setCompanion")

    val nullableReferenceSchemaFieldClass = "NullableReferenceSchemaField".runtimeClass(RUNTIME_SCHEMA_FIELD_PACKAGE)
    val nullableReferenceSchemaFieldSetCompanion = nullableReferenceSchemaFieldClass.functionByName("setCompanion")

    val referenceListSchemaFieldClass = "ReferenceListSchemaField".runtimeClass(RUNTIME_SCHEMA_FIELD_PACKAGE)
    val referenceListSchemaFieldClassSetCompanion = referenceListSchemaFieldClass.functionByName("setCompanion")

    val schematicSchemaFieldClass = "SchematicSchemaField".runtimeClass(RUNTIME_SCHEMA_FIELD_PACKAGE)
    val schematicSchemaFieldSetCompanion = schematicSchemaFieldClass.functionByName("setCompanion")

    val nullableSchematicSchemaFieldClass = "NullableSchematicSchemaField".runtimeClass(RUNTIME_SCHEMA_FIELD_PACKAGE)
    val nullableSchematicSchemaFieldSetCompanion = nullableSchematicSchemaFieldClass.functionByName("setCompanion")

    val schematicListSchemaFieldClass = "SchematicListSchemaField".runtimeClass(RUNTIME_SCHEMA_FIELD_PACKAGE)
    val schematicListSchemaFieldClassSetCompanion = schematicListSchemaFieldClass.functionByName("setCompanion")

    val schematicCompanionClass = Strings.SCHEMATIC_COMPANION_CLASS.runtimeClass()
    val schematicCompanionEncodeProto = schematicCompanionClass.functionByName(ENCODE_PROTO)
    val schematicCompanionDecodeProto = schematicCompanionClass.functionByName(DECODE_PROTO)
    val schematicCompanionNewInstance = schematicCompanionClass.functionByName(SCHEMATIC_COMPANION_NEW_INSTANCE)
    val schematicCompanionSetFieldValue = schematicCompanionClass.functionByName(SCHEMATIC_COMPANION_SET_FIELD_VALUE)
    val schematicCompanionGetFieldValue = schematicCompanionClass.functionByName(SCHEMATIC_COMPANION_GET_FIELD_VALUE)

    val mutableMapGet = irContext.irBuiltIns.mutableMapClass.functionByName("get").owner.symbol

    val typeSystem = IrTypeSystemContextImpl(irContext.irBuiltIns)

    val funCache = SchematicFunctionCache(this)

    val protoMessageType = PROTO_MESSAGE_CLASS.runtimeClass(PROTO_PACKAGE).defaultType

}