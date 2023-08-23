package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

var phoneRegex = Regex("\\+[0-9]{1,3}.[0-9]{4,14}(x[0-9]{1,5})?")

interface PhoneNumberSchemaFieldCommon {
    var blank : Boolean?

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): String? {
        return when (anyValue) {
            is String -> anyValue
            null -> {
                fails += fail(validationStrings.nullFail)
                null
            }
            else -> anyValue.toString()
        }
    }

    fun validateValueCommon(value: String, fails: MutableList<ValidationFailInfo>) {
        if (blank == false && value.isBlank()) fails += fail(validationStrings.blankFail)
        if (value.isNotBlank() && !phoneRegex.matches(value)) fails += fail(validationStrings.patternFail)
    }
}

/**
 * Contains a phone number according to the Extensible Provisioning Protocol.
 *
 * https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s03.html#:~:text=Thanks%20to%20the%20international%20phone,in%20use%20contain%20seven%20digits.
 *
 * ```text
 * +CCC.NNNNNNNNNNNNNNxEEEEE
 * ```
 */
open class PhoneNumberSchemaField(
    override var blank : Boolean?
) : SchemaField<String>, PhoneNumberSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Phone
    override var isNullable: Boolean = false
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

    infix fun blank(value: Boolean): PhoneNumberSchemaField {
        this.blank = value
        return this
    }

    fun nullable() : NullablePhoneNumberSchemaField {
        return NullablePhoneNumberSchemaField(blank)
    }

}

open class NullablePhoneNumberSchemaField(
    override var blank : Boolean?
) : SchemaField<String?>, PhoneNumberSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Phone
    override var isNullable: Boolean = true
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
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.stringOrNull(fieldNumber, fieldNumber + 1, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.stringOrNull(fieldNumber, fieldNumber + 1)
        schematic.schematicValues[name] = value
    }

    infix fun blank(value: Boolean): NullablePhoneNumberSchemaField {
        this.blank = value
        return this
    }

    fun nullable() : NullablePhoneNumberSchemaField {
        return this
    }

}