package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

interface IntSchemaFieldCommon {
    var min: Int?
    var max: Int?

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Int? {
        return when (anyValue) {
            is Int -> anyValue
            is Number -> anyValue.toInt()
            is String -> anyValue.toIntOrNull()
            else -> {
                fails += fail(validationStrings.integerFail)
                null
            }
        }
    }

    fun validateValueCommon(value: Int, fails: MutableList<ValidationFailInfo>) {
        min?.let { if (value < it) fails += fail(validationStrings.minValueFail, it) }
        max?.let { if (value > it) fails += fail(validationStrings.maxValueFail, it) }
    }
}

open class IntSchemaField(
    override var definitionDefault: Int?,
    override var min: Int?,
    override var max: Int?
) : SchemaField<Int>, IntSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Int
    override val isNullable: Boolean = false
    override val naturalDefault = 0

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Int? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: Int, fails: MutableList<ValidationFailInfo>) {
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.int(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.int(fieldNumber)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Int): IntSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun min(value: Int): IntSchemaField {
        this.min = value
        return this
    }

    infix fun max(value: Int): IntSchemaField {
        this.max = value
        return this
    }

    fun nullable(): NullableIntSchemaField {
        return NullableIntSchemaField(definitionDefault, min, max)
    }

}

open class NullableIntSchemaField(
    override var definitionDefault: Int?,
    override var min: Int?,
    override var max: Int?
) : SchemaField<Int?>, IntSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Int
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Int? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: Int?, fails: MutableList<ValidationFailInfo>) {
        if (value == null) return
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.intOrNull(fieldNumber, fieldNumber + 1, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.intOrNull(fieldNumber, fieldNumber + 1)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Int): NullableIntSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun min(value: Int): NullableIntSchemaField {
        this.min = value
        return this
    }

    infix fun max(value: Int): NullableIntSchemaField {
        this.max = value
        return this
    }

    fun nullable(): NullableIntSchemaField {
        return this
    }

}