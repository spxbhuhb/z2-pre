package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

class EmailSchemaField(
    override val name: String,
    override val nullable: Boolean,
    val blank : Boolean?
) : SchemaField<String> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Email

    override val definitionDefault = ""
    override val naturalDefault = ""

    // FIXME email regex should be expect/actual and should handle unicode
    val minLength = 6 // a@xx.yy
    val maxLength = 254 // https://stackoverflow.com/a/574698/3796844
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,63}")

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        if (anyValue == null) return null

        return when (anyValue) {
            is String -> anyValue
            else -> anyValue.toString()
        }
    }

    override fun validateNotNullable(value: String, fails: MutableList<ValidationFailInfo>) {
        val length = value.length

        if (length < minLength) fails += fail(validationStrings.minLengthFail, minLength)
        if (length > maxLength) fails += fail(validationStrings.maxValueFail, maxLength)
        if (blank == false && value.isBlank()) fails += fail(validationStrings.blankFail)
        if (!emailRegex.matches(value)) fails += fail(validationStrings.patternFail)
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