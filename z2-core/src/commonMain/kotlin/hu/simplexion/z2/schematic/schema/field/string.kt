package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicList
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder

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
        builder.string(fieldNumber, name, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.string(fieldNumber, name)
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

    override val type: SchemaFieldType
        get() = SchemaFieldType.String

    override val isNullable
        get() = true

    override val naturalDefault
        get() = null

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
        builder.stringOrNull(fieldNumber, name, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.stringOrNull(fieldNumber, name)
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
) : ListSchemaField<String, StringSchemaField>(
    StringSchemaField(null, minLength, maxLength, blank, pattern)
) {

    override var name: String = ""

    override var definitionDefault = definitionDefault?.let { SchematicList(definitionDefault, this) }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.stringList(fieldNumber, name, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.stringList(fieldNumber, name).toMutableList()
        schematic.schematicValues[name] = SchematicList(value, this).also { it.schematicState.parent = schematic }
    }

    infix fun default(value: MutableList<String>?): StringListSchemaField {
        definitionDefault = value?.let { SchematicList(it, this) }
        return this
    }

}