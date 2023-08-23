package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoLocalDate
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings
import kotlinx.datetime.LocalDate

interface LocalDateSchemaFieldCommon {
     fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDate? {
        return when (anyValue) {
            is LocalDate -> anyValue
            is String -> LocalDate.parse(anyValue)
            else -> {
                fails += fail(validationStrings.localDateFail)
                null
            }
        }
    }
}

open class LocalDateSchemaField(
    override var definitionDefault: LocalDate?
) : SchemaField<LocalDate>, LocalDateSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.LocalDate
    override val isNullable: Boolean = false
    override val naturalDefault = LocalDate.fromEpochDays(0)

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDate? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, ProtoLocalDate, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, ProtoLocalDate)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: LocalDate?): LocalDateSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable() : NullableLocalDateSchemaField {
        return NullableLocalDateSchemaField(definitionDefault)
    }

}

open class NullableLocalDateSchemaField(
    override var definitionDefault: LocalDate?
) : SchemaField<LocalDate?>, LocalDateSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.LocalDate
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDate? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instanceOrNull(fieldNumber, fieldNumber + 1, ProtoLocalDate, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instanceOrNull(fieldNumber, fieldNumber + 1, ProtoLocalDate)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: LocalDate?): NullableLocalDateSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable() : NullableLocalDateSchemaField {
        return this
    }

}