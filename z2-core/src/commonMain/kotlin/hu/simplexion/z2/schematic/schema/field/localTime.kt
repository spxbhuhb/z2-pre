package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.protobuf.ProtoLocalTime
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder
import kotlinx.datetime.LocalTime

interface LocalTimeSchemaFieldCommon {
     fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalTime? {
        return when (anyValue) {
            is LocalTime -> anyValue
            is String -> LocalTime.parse(anyValue)
            else -> {
                fails += fail(validationStrings.localTimeFail)
                null
            }
        }
    }
}

open class LocalTimeSchemaField(
    override var definitionDefault: LocalTime?
) : SchemaField<LocalTime>, LocalTimeSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.LocalTime
    override val isNullable: Boolean = false
    override val naturalDefault = LocalTime.fromSecondOfDay(0)

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalTime? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, ProtoLocalTime, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, ProtoLocalTime)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: LocalTime?): LocalTimeSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable() : NullableLocalTimeSchemaField {
        return NullableLocalTimeSchemaField(definitionDefault)
    }

}

open class NullableLocalTimeSchemaField(
    override var definitionDefault: LocalTime?
) : SchemaField<LocalTime?>, LocalTimeSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.LocalTime
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalTime? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.instanceOrNull(fieldNumber, fieldNumber + 1, ProtoLocalTime, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instanceOrNull(fieldNumber, fieldNumber + 1, ProtoLocalTime)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: LocalTime?): NullableLocalTimeSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable() : NullableLocalTimeSchemaField {
        return this
    }

}