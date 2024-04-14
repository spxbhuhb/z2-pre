package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.builtin.LocalDateCoder
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
    override val naturalDefault
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDate? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, name, LocalDateCoder, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, name, LocalDateCoder)
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
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.instanceOrNull(fieldNumber, name, LocalDateCoder, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instanceOrNull(fieldNumber, name, LocalDateCoder)
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