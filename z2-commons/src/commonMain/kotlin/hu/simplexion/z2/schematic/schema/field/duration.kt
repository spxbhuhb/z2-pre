package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoDuration
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import kotlin.time.Duration

interface DurationSchemaFieldCommon {

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Duration? {
        return when (anyValue) {
            is Duration -> anyValue
            is String -> Duration.parse(anyValue)
            else -> {
                fails += fail(validationStrings.durationFail)
                null
            }
        }
    }

}

open class DurationSchemaField(
    override var definitionDefault: Duration?
) : SchemaField<Duration>, DurationSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Duration
    override val isNullable: Boolean = false
    override val naturalDefault = Duration.ZERO

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Duration? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, ProtoDuration, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, ProtoDuration)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Duration?): DurationSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable(): NullableDurationSchemaField {
        return NullableDurationSchemaField(definitionDefault)
    }

}

open class NullableDurationSchemaField(
    override var definitionDefault: Duration?
) : SchemaField<Duration?>, DurationSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Duration
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Duration? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.instanceOrNull(fieldNumber, fieldNumber + 1, ProtoDuration, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instanceOrNull(fieldNumber, fieldNumber + 1, ProtoDuration)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Duration?): NullableDurationSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable(): NullableDurationSchemaField {
        return this
    }

}