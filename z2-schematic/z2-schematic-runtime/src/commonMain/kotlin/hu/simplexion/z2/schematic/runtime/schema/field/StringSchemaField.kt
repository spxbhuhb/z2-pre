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
    override val name: String,
    override val nullable: Boolean,
    override val definitionDefault: String?,
    val minLength: Int?,
    val maxLength: Int?,
    val blank : Boolean?,
    val pattern : Regex?
) : SchemaField<String> {

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

        if (minLength != null && length < minLength) fails += fail(validationStrings.minLengthFail, minLength)
        if (maxLength != null && length > maxLength) fails += fail(validationStrings.maxValueFail, maxLength)
        if (blank == false && value.isBlank()) fails += fail(validationStrings.blankFail)
        if (pattern != null && !pattern.matches(value)) fails += fail(validationStrings.patternFail)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.string(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.string(fieldNumber)
        schematic.schematicValues[name] = value
    }

}