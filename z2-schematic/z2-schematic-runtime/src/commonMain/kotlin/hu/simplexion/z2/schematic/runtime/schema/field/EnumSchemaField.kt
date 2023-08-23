package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

class EnumSchemaField<E : Enum<E>>(
    override val name: String,
    override val nullable: Boolean,
    val entries: Array<E>,
    override val definitionDefault: E?
) : SchemaField<E> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Enum

    override val naturalDefault = entries.first()

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): E? {
        if (anyValue == null) return null

        @Suppress("UNCHECKED_CAST")
        if (anyValue::class == naturalDefault::class) return anyValue as E

        return when (anyValue) {
            is Number -> entries[anyValue.toInt()]
            is String -> entries.first { it.name == anyValue }
            else -> {
                fails += fail(validationStrings.enumFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: E, fails: MutableList<ValidationFailInfo>) {

    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.int(fieldNumber, value.ordinal)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.int(fieldNumber)
        schematic.schematicValues[name] = entries[value]
    }

}