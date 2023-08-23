package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

/**
 * Contains a phone number according to the Extensible Provisioning Protocol.
 *
 * https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s03.html#:~:text=Thanks%20to%20the%20international%20phone,in%20use%20contain%20seven%20digits.
 *
 * ```text
 * +CCC.NNNNNNNNNNNNNNxEEEEE
 * ```
 */
class PhoneNumberSchemaField(
    override val name: String,
    override val nullable: Boolean,
    val blank : Boolean?
) : SchemaField<String> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Phone

    override val definitionDefault = ""
    override val naturalDefault = ""

    val phoneRegex = Regex("\\+[0-9]{1,3}.[0-9]{4,14}(x[0-9]{1,5})?")

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        if (anyValue == null) return null

        return when (anyValue) {
            is String -> anyValue
            else -> anyValue.toString()
        }
    }

    override fun validateNotNullable(value: String, fails: MutableList<ValidationFailInfo>) {
        if (blank == false && value.isBlank()) fails += fail(validationStrings.blankFail)
        if (value.isNotBlank() && !phoneRegex.matches(value)) fails += fail(validationStrings.patternFail)
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