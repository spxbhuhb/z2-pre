package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

class BooleanSchemaField(
    override val name: String,
    override val nullable: Boolean,
    override val definitionDefault: Boolean?
) : SchemaField<Boolean> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Boolean

    override val naturalDefault = false

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Boolean? {
        if (anyValue == null) return null

        return when (anyValue) {
            is Boolean -> anyValue
            is String -> anyValue.toBooleanStrict()
            else -> {
                fails += fail(validationStrings.booleanFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: Boolean, fails: MutableList<ValidationFailInfo>) {

    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.boolean(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.boolean(fieldNumber)
        schematic.schematicValues[name] = value
    }

}