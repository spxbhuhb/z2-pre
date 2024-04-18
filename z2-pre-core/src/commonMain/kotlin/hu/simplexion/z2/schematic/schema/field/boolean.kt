package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder

interface BooleanSchemaFieldCommon {

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Boolean? {
        return when (anyValue) {
            is Boolean -> anyValue
            is String -> anyValue.toBooleanStrict()
            else -> {
                fails += fail(validationStrings.booleanFail)
                null
            }
        }
    }

}

open class BooleanSchemaField(
    override var definitionDefault: Boolean?
) : SchemaField<Boolean>, BooleanSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Boolean
    override val isNullable: Boolean = false
    override val naturalDefault = false

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Boolean? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.boolean(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.boolean(fieldNumber)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Boolean?): BooleanSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable(): NullableBooleanSchemaField {
        return NullableBooleanSchemaField(definitionDefault)
    }

}

open class NullableBooleanSchemaField(
    override var definitionDefault: Boolean?
) : SchemaField<Boolean?>, BooleanSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Boolean
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Boolean? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.booleanOrNull(fieldNumber, fieldNumber + 1, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.booleanOrNull(fieldNumber, fieldNumber + 1)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Boolean?): NullableBooleanSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable(): NullableBooleanSchemaField {
        return this
    }

}