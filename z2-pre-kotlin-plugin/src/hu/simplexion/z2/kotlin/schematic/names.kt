package hu.simplexion.z2.kotlin.schematic

import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

object Strings {
    const val SCHEMATIC_RUNTIME_PACKAGE = "hu.simplexion.z2.schematic"

    const val SCHEMATIC_CLASS = "Schematic"

    const val SCHEMATIC_FQNAME_PROPERTY = "schematicFqName"
    const val SCHEMATIC_COMPANION_CLASS = "SchematicCompanion"
    const val SCHEMATIC_SCHEMA_PROPERTY = "schematicSchema"
    const val SCHEMATIC_COMPANION_PROPERTY = "schematicCompanion"

    const val SCHEMATIC_SCHEMA_PACKAGE = "hu.simplexion.z2.schematic.schema"
    const val SCHEMATIC_SCHEMA_CLASS = "Schema"

    const val SCHEMATIC_ENTITY_PACKAGE = "hu.simplexion.z2.schematic.entity"
    const val SCHEMATIC_ENTITY_CLASS = "SchematicEntity"
    const val SCHEMATIC_ENTITY_STORE_CLASS = "SchematicEntityStore"
    const val SCHEMATIC_ENTITY_COMPANION_CLASS = "SchematicEntityCompanion"
    const val SCHEMATIC_ENTITY_STORE_PROPERTY = "schematicEntityStore"
    const val SCHEMATIC_ENTITY_COMPANION_PROPERTY = "schematicEntityCompanion"

    const val SCHEMATIC_PATTERN = "$SCHEMATIC_CLASS<"
    const val SCHEMATIC_ENTITY_PATTERN = "$SCHEMATIC_ENTITY_CLASS<"
    const val SCHEMATIC_COMPANION_PATTERN = "$SCHEMATIC_COMPANION_CLASS<"
    const val SCHEMATIC_ENTITY_COMPANION_PATTERN = "$SCHEMATIC_ENTITY_COMPANION_CLASS<"
}

object Names {
    val SCHEMATIC_FQNAME_PROPERTY = Name.identifier(Strings.SCHEMATIC_FQNAME_PROPERTY)
    val SCHEMATIC_SCHEMA_PROPERTY = Name.identifier(Strings.SCHEMATIC_SCHEMA_PROPERTY)
    val SCHEMATIC_ENTITY_STORE_PROPERTY = Name.identifier(Strings.SCHEMATIC_ENTITY_STORE_PROPERTY)
    val SCHEMATIC_COMPANION_PROPERTY = Name.identifier(Strings.SCHEMATIC_COMPANION_PROPERTY)
    val SCHEMATIC_ENTITY_COMPANION_PROPERTY = Name.identifier(Strings.SCHEMATIC_ENTITY_COMPANION_PROPERTY)

    val SCHEMATIC_SCHEMA_CLASS = Name.identifier(Strings.SCHEMATIC_SCHEMA_CLASS)
    val SCHEMATIC_COMPANION_CLASS = Name.identifier(Strings.SCHEMATIC_COMPANION_CLASS)
    val SCHEMATIC_ENTITY_COMPANION_CLASS = Name.identifier(Strings.SCHEMATIC_ENTITY_COMPANION_CLASS)
    val SCHEMATIC_ENTITY_STORE_CLASS = Name.identifier(Strings.SCHEMATIC_ENTITY_STORE_CLASS)
}

object FqNames {
    val SCHEMATIC_RUNTIME_PACKAGE = FqName(Strings.SCHEMATIC_RUNTIME_PACKAGE)
    val SCHEMATIC_SCHEMA_PACKAGE = FqName(Strings.SCHEMATIC_SCHEMA_PACKAGE)
    val SCHEMATIC_ENTITY_PACKAGE = FqName(Strings.SCHEMATIC_ENTITY_PACKAGE)
}

object ClassIds {
    val SCHEMATIC_SCHEMA = ClassId(FqNames.SCHEMATIC_SCHEMA_PACKAGE, Names.SCHEMATIC_SCHEMA_CLASS)
    val SCHEMATIC_COMPANION = ClassId(FqNames.SCHEMATIC_RUNTIME_PACKAGE, Names.SCHEMATIC_COMPANION_CLASS)
    val SCHEMATIC_ENTITY_COMPANION = ClassId(FqNames.SCHEMATIC_ENTITY_PACKAGE, Names.SCHEMATIC_ENTITY_COMPANION_CLASS)
    val SCHEMATIC_ENTITY_STORE = ClassId(FqNames.SCHEMATIC_ENTITY_PACKAGE, Names.SCHEMATIC_ENTITY_STORE_CLASS)
}

const val FIELD_DEFINITION_FUNCTION_CLASS = "FieldDefinitionFunction"
const val DEFINITION_TRANSFORM_FUNCTION_CLASS = "DefinitionTransformFunction"
const val SCHEMATIC_ACCESS_FUNCTION_CLASS = "SchematicAccessFunction"
const val SCHEMA_FIELD_NOT_FOUND_CLASS = "SchemaFieldNotFound"
const val SCHEMA_FIELD_IS_IMMUTABLE = "SchemaFieldIsImmutable"

const val SCHEMATIC_VALUES_PROPERTY = "schematicValues"
const val SCHEMATIC_CHANGE = "schematicChange"
const val SCHEMATIC_COMPANION_NAME = "Companion"
const val SCHEMATIC_COMPANION_NEW_INSTANCE = "newInstance"
const val SCHEMATIC_COMPANION_SET_FIELD_VALUE = "setFieldValue"
const val SCHEMATIC_COMPANION_GET_FIELD_VALUE = "getFieldValue"
const val SCHEMATIC_TO_ACCESS_CONTEXT = "toSchematicAccessContext"

const val TO_ACCESS_CONTEXT_ARG_COUNT = 2
const val TO_ACCESS_CONTEXT_SCHEMATIC_INDEX = 1
const val TO_ACCESS_CONTEXT_FIELD_NAME_INDEX = 2

const val SCHEMATIC_CHANGE_FIELD_INDEX_INDEX = 0
const val SCHEMATIC_CHANGE_VALUE_INDEX = 1

const val FDF_ANNOTATION_FIELD_CLASS_INDEX = 0

const val FIELD_CONSTRUCTOR_NAME_INDEX = 0
const val FIELD_CONSTRUCTOR_NULLABLE_INDEX = 1
const val FIELD_CONSTRUCTOR_VARARG_INDEX = 2

const val SCHEMA_FIELD_CLASS = "SchemaField"
const val SCHEMA_FIELD_SET_FIELD_NAME = "setFieldName"

const val RUNTIME_SCHEMA_FIELD_PACKAGE = "hu.simplexion.z2.schematic.schema.field"

const val NULLABLE_SCHEMATIC_SCHEMA_FIELD_CLASS = "NullableSchematicSchemaField"
const val SCHEMATIC_LIST_SCHEMA_FIELD_CLASS = "SchematicListSchemaField"

const val SET_COMPANION = "setCompanion"

const val RUNTIME_CONTEXT_PACKAGE = "hu.simplexion.z2.schematic.access"
const val SCHEMATIC_ACCESS_CONTEXT = "SchematicAccessContext"

const val PROTO_PACKAGE = "hu.simplexion.z2.serialization.protobuf"

const val PROTO_MESSAGE_CLASS = "ProtoMessage"

const val ENCODE_PROTO = "encodeProto"
const val DECODE_PROTO = "decodeProto"

const val ENCODE_PROTO_VALUE_NAME = "value"
const val DECODE_PROTO_MESSAGE_NAME = "message"