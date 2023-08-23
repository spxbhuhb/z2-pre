package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

interface LongSchemaFieldDefault {
    var min: Long?
    var max: Long?

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Long? {
        return when (anyValue) {
            is Long -> anyValue
            is Number -> anyValue.toLong()
            is String -> anyValue.toLongOrNull()
            else -> {
                fails += fail(validationStrings.integerFail)
                null
            }
        }
    }

    fun validateValueCommon(value: Long, fails: MutableList<ValidationFailInfo>) {
        min?.let { if (value < it) fails += fail(validationStrings.minValueFail, it) }
        max?.let { if (value > it) fails += fail(validationStrings.maxValueFail, it) }
    }
}

open class LongSchemaField(
    override var definitionDefault: Long?,
    override var min: Long?,
    override var max: Long?
) : SchemaField<Long>, LongSchemaFieldDefault {

    override val type: SchemaFieldType get() = SchemaFieldType.Long
    override var isNullable: Boolean = false
    override val naturalDefault = 0L

    override var name: String = ""

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

    infix fun default(value: Long?): LongSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun min(value: Long): LongSchemaField {
        this.min = value
        return this
    }

    infix fun max(value: Long): LongSchemaField {
        this.max = value
        return this
    }

    fun nullable(): NullableLongSchemaField {
        return NullableLongSchemaField(definitionDefault, min, max)
    }

}

open class NullableLongSchemaField(
    override var definitionDefault: Long?,
    override var min: Long?,
    override var max: Long?
) : SchemaField<Long?>, LongSchemaFieldDefault {

    override val type: SchemaFieldType get() = SchemaFieldType.Long
    override var isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Long? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: Long?, fails: MutableList<ValidationFailInfo>) {
        if (value == null) return
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.longOrNull(fieldNumber, fieldNumber + 1, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.longOrNull(fieldNumber, fieldNumber + 1)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Long?): NullableLongSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun min(value: Long): NullableLongSchemaField {
        this.min = value
        return this
    }

    infix fun max(value: Long): NullableLongSchemaField {
        this.max = value
        return this
    }

    fun nullable(): NullableLongSchemaField {
        return this
    }

}