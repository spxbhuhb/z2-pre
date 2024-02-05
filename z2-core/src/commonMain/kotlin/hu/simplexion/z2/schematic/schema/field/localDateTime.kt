package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.builtin.LocalDateTimeCoder
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder
import kotlinx.datetime.LocalDateTime

interface LocalDateTimeSchemaFieldCommon {
    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDateTime? {
        if (anyValue == null) return null

        return when (anyValue) {
            is LocalDateTime -> anyValue
            is String -> LocalDateTime.parse(anyValue)
            else -> {
                fails += fail(validationStrings.instantFail)
                null
            }
        }
    }
}

open class LocalDateTimeSchemaField(
    override var definitionDefault: LocalDateTime?
) : SchemaField<LocalDateTime>, LocalDateTimeSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.LocalDateTime
    override var isNullable: Boolean = false
    override val naturalDefault = LocalDateTime(0, 1, 1, 0, 0, 0, 0)

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDateTime? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, LocalDateTimeCoder, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, LocalDateTimeCoder)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: LocalDateTime?): LocalDateTimeSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable(): NullableLocalDateTimeSchemaField {
        return NullableLocalDateTimeSchemaField(definitionDefault)
    }

}

open class NullableLocalDateTimeSchemaField(
    override var definitionDefault: LocalDateTime?
) : SchemaField<LocalDateTime?>, LocalDateTimeSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.LocalDateTime
    override var isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDateTime? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.instanceOrNull(fieldNumber, fieldNumber + 1, LocalDateTimeCoder, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instanceOrNull(fieldNumber, fieldNumber + 1, LocalDateTimeCoder)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: LocalDateTime?): NullableLocalDateTimeSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable(): NullableLocalDateTimeSchemaField {
        return this
    }

}