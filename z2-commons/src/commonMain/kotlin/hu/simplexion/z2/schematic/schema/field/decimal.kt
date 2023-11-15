package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.localization.locales.AbstractLocalizedFormats
import hu.simplexion.z2.localization.locales.localizedFormats
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings

interface DecimalSchemaFieldDefault {
    val scale : Int
    val precision : Int
    val shift : Double
    var min: Long?
    var max: Long?

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Long? {
        return when (anyValue) {
            is Long -> anyValue
            is Int -> anyValue.toLong()
            is Double -> (anyValue * shift).toLong()
            is String -> localizedFormats.toDoubleOrNull(anyValue)?.let { it * shift }?.toLong()
            else -> {
                fails += fail(validationStrings.numericFail)
                null
            }
        }
    }

    fun validateValueCommon(value: Long, fails: MutableList<ValidationFailInfo>) {
        min?.let { if (value / shift < it) fails += fail(validationStrings.minValueFail, it) }
        max?.let { if (value / shift > it) fails += fail(validationStrings.maxValueFail, it) }
    }
}

open class DecimalSchemaField(
    final override val scale : Int,
    final override val precision: Int,
    override var definitionDefault: Long?,
    override var min: Long?,
    override var max: Long?
) : SchemaField<Long>, DecimalSchemaFieldDefault {

    override val type: SchemaFieldType get() = SchemaFieldType.Decimal
    override var isNullable: Boolean = false
    override val naturalDefault = 0L

    override var name: String = ""

    override val shift = AbstractLocalizedFormats.shifts[scale]

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Long? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: Long, fails: MutableList<ValidationFailInfo>) {
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.long(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.long(fieldNumber)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Long?): DecimalSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun min(value: Long): DecimalSchemaField {
        this.min = value
        return this
    }

    infix fun max(value: Long): DecimalSchemaField {
        this.max = value
        return this
    }

    fun nullable(): NullableDecimalSchemaField {
        return NullableDecimalSchemaField(scale, precision, definitionDefault, min, max)
    }

}

open class NullableDecimalSchemaField(
    final override val scale : Int,
    final override val precision: Int,
    override var definitionDefault: Long?,
    override var min: Long?,
    override var max: Long?
) : SchemaField<Long?>, DecimalSchemaFieldDefault {

    override val type: SchemaFieldType get() = SchemaFieldType.Decimal
    override var isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override val shift = AbstractLocalizedFormats.shifts[scale]

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Long? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: Long?, fails: MutableList<ValidationFailInfo>) {
        if (value == null) return
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.longOrNull(fieldNumber, fieldNumber + 1, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.longOrNull(fieldNumber, fieldNumber + 1)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Long?): NullableDecimalSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun min(value: Long): NullableDecimalSchemaField {
        this.min = value
        return this
    }

    infix fun max(value: Long): NullableDecimalSchemaField {
        this.max = value
        return this
    }

    fun nullable(): NullableDecimalSchemaField {
        return this
    }

}