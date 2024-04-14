package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.builtin.LocalTimeCoder
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface LocalTimeSchemaFieldCommon {
    var min: LocalTime?
    var max: LocalTime?

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

    fun validateValueCommon(value: LocalTime, fails: MutableList<ValidationFailInfo>) {
        min?.let { if (value < it) fails += fail(validationStrings.minValueFail, it) }
        max?.let { if (value > it) fails += fail(validationStrings.maxValueFail, it) }
    }
}

open class LocalTimeSchemaField(
    override var definitionDefault: LocalTime?,
    override var min: LocalTime?,
    override var max: LocalTime?
) : SchemaField<LocalTime>, LocalTimeSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.LocalTime
    override val isNullable: Boolean = false
    override val naturalDefault
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalTime? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: LocalTime, fails: MutableList<ValidationFailInfo>) {
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, name, LocalTimeCoder, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, name, LocalTimeCoder)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: LocalTime?): LocalTimeSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable() : NullableLocalTimeSchemaField {
        return NullableLocalTimeSchemaField(definitionDefault, min, max)
    }

}

open class NullableLocalTimeSchemaField(
    override var definitionDefault: LocalTime?,
    override var min: LocalTime?,
    override var max: LocalTime?
) : SchemaField<LocalTime?>, LocalTimeSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.LocalTime
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalTime? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: LocalTime?, fails: MutableList<ValidationFailInfo>) {
        if (value == null) return
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.instanceOrNull(fieldNumber, name, LocalTimeCoder, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instanceOrNull(fieldNumber, name, LocalTimeCoder)
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