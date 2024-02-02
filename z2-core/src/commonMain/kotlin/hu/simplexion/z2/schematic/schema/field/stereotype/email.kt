package hu.simplexion.z2.schematic.schema.field.stereotype

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder

private val minLength = 6 // a@xx.yy
private val maxLength = 254 // https://stackoverflow.com/a/574698/3796844
private val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,63}")

interface EmailSchemaFieldCommon {
    
    var blank : Boolean?

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        if (anyValue == null) return null

        return when (anyValue) {
            is String -> anyValue
            else -> anyValue.toString()
        }
    }

    fun validateValueCommon(value: String, fails: MutableList<ValidationFailInfo>) {
        val length = value.length

        if (blank == false || value.isNotEmpty()) {
            if (length < minLength) fails += fail(validationStrings.minLengthFail, minLength)
            if (value.isBlank()) fails += fail(validationStrings.blankFail)
            if (! emailRegex.matches(value)) fails += fail(validationStrings.patternFail)
        }
        if (length > maxLength) fails += fail(validationStrings.maxValueFail, maxLength)
    }

}    

open class EmailSchemaField(
    override var blank: Boolean?
) : SchemaField<String>, EmailSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Email
    override val isNullable: Boolean = false
    override val definitionDefault = ""
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

    infix fun blank(value : Boolean): EmailSchemaField {
        this.blank = value
        return this
    }

    fun nullable() : NullableEmailSchemaField {
        return NullableEmailSchemaField(blank)
    }
}

open class NullableEmailSchemaField(
    override var blank: Boolean?
) : SchemaField<String?>, EmailSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Email
    override val isNullable: Boolean = true
    override val definitionDefault = null
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        if (anyValue == null) return null
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

    infix fun blank(value : Boolean): NullableEmailSchemaField {
        this.blank = value
        return this
    }

    fun nullable() : NullableEmailSchemaField {
        return this
    }
}