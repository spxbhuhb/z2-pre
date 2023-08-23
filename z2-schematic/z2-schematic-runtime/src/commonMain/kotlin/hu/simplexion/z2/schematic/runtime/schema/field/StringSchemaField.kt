package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

open class StringSchemaField(
    override var definitionDefault: String?,
    var minLength: Int?,
    var maxLength: Int?,
    var blank: Boolean?,
    var pattern: Regex?
) : SchemaField<String> {

    override var name: String = ""
    override var nullable: Boolean = false

    override val type: SchemaFieldType
        get() = SchemaFieldType.String

    override val naturalDefault = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        if (anyValue == null) return null

        return when (anyValue) {
            is String -> anyValue
            else -> anyValue.toString()
        }
    }

    override fun validateNotNullable(value: String, fails: MutableList<ValidationFailInfo>) {
        val length = value.length

        minLength?.let { if (length < it) fails += fail(validationStrings.minLengthFail, it) }
        maxLength?.let { if (length > it) fails += fail(validationStrings.maxValueFail, it) }
        if (blank == false && value.isBlank()) fails += fail(validationStrings.blankFail)
        pattern?.let { if (! it.matches(value)) fails += fail(validationStrings.patternFail) }
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

}