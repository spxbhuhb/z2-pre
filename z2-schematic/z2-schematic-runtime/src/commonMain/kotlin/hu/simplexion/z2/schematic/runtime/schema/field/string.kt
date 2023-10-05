package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicList
import hu.simplexion.z2.schematic.runtime.schema.ListSchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

interface StringSchemaFieldCommon {
    var minLength: Int?
    var maxLength: Int?
    var blank: Boolean?
    var pattern: Regex?

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        if (anyValue == null) return null

        return when (anyValue) {
            is String -> anyValue
            else -> anyValue.toString()
        }
    }

    fun validateValueCommon(value: String, fails: MutableList<ValidationFailInfo>) {
        val length = value.length

        minLength?.let { if (length < it) fails += fail(validationStrings.minLengthFail, it) }
        maxLength?.let { if (length > it) fails += fail(validationStrings.maxValueFail, it) }
        if (blank == false && value.isBlank()) fails += fail(validationStrings.blankFail)
        pattern?.let { if (! it.matches(value)) fails += fail(validationStrings.patternFail) }
    }
}

open class StringSchemaField(
    override var definitionDefault: String?,
    override var minLength: Int?,
    override var maxLength: Int?,
    override var blank: Boolean?,
    override var pattern: Regex?
) : SchemaField<String>, StringSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.String
    override val isNullable: Boolean = false
    override val naturalDefault = ""

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: String, fails: MutableList<ValidationFailInfo>) {
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.string(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.string(fieldNumber)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: String?): StringSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun minLength(value: Int): StringSchemaField {
        this.minLength = value
        return this
    }

    infix fun maxLength(value: Int): StringSchemaField {
        this.maxLength = value
        return this
    }

    infix fun blank(value: Boolean): StringSchemaField {
        this.blank = value
        return this
    }

    infix fun pattern(value: Regex): StringSchemaField {
        this.pattern = value
        return this
    }

    open fun nullable(): NullableStringSchemaField {
        return NullableStringSchemaField(definitionDefault, minLength, maxLength, blank, pattern)
    }

}

open class NullableStringSchemaField(
    override var definitionDefault: String?,
    override var minLength: Int?,
    override var maxLength: Int?,
    override var blank: Boolean?,
    override var pattern: Regex?
) : SchemaField<String?>, StringSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.String
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: String?, fails: MutableList<ValidationFailInfo>) {
        if (value == null) return
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.stringOrNull(fieldNumber, fieldNumber + 1, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.stringOrNull(fieldNumber, fieldNumber + 1)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: String?): NullableStringSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun minLength(value: Int): NullableStringSchemaField {
        this.minLength = value
        return this
    }

    infix fun maxLength(value: Int): NullableStringSchemaField {
        this.maxLength = value
        return this
    }

    infix fun blank(value: Boolean): NullableStringSchemaField {
        this.blank = value
        return this
    }

    infix fun pattern(value: Regex): NullableStringSchemaField {
        this.pattern = value
        return this
    }

    open fun nullable(): NullableStringSchemaField {
        return this
    }

}

class StringListSchemaField(
    definitionDefault: MutableList<String>?,
    minLength: Int?,
    maxLength: Int?,
    blank: Boolean?,
    pattern: Regex?
) : ListSchemaField<String> {

    override var name: String = ""

    override val itemSchemaField = StringSchemaField(null, minLength, maxLength, blank, pattern)

    override var definitionDefault = definitionDefault?.let { SchematicList(null, definitionDefault, this) }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.stringList(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.stringList(fieldNumber).toMutableList()
        schematic.schematicValues[name] = SchematicList(schematic, value, this)
    }

    infix fun default(value: MutableList<String>?): StringListSchemaField {
        definitionDefault = value?.let { SchematicList(null, it, this) }
        return this
    }

    infix fun minLength(value: Int): StringListSchemaField {
        itemSchemaField.minLength = value
        return this
    }

    infix fun maxLength(value: Int): StringListSchemaField {
        itemSchemaField.maxLength = value
        return this
    }

    infix fun blank(value: Boolean): StringListSchemaField {
        itemSchemaField.blank = value
        return this
    }

    infix fun pattern(value: Regex): StringListSchemaField {
        itemSchemaField.pattern = value
        return this
    }
}