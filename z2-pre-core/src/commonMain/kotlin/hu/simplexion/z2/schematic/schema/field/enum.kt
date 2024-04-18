package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder

interface EnumSchemaFieldCommon<E : Enum<E>> {
    val entries: Array<E>
    val naturalDefault: E?

    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): E? {
        if (anyValue == null) {
            fails += fail(validationStrings.enumFail)
            return null
        }

        @Suppress("UNCHECKED_CAST")
        if (anyValue::class == entries.first()::class) return anyValue as E

        return when (anyValue) {
            is Number -> entries[anyValue.toInt()]
            is String -> entries.first { it.name == anyValue }
            else -> {
                fails += fail(validationStrings.enumFail)
                null
            }
        }
    }
}

open class EnumSchemaField<E : Enum<E>>(
    override val entries: Array<E>,
    override var definitionDefault: E?
) : SchemaField<E>, EnumSchemaFieldCommon<E> {

    override val type: SchemaFieldType get() = SchemaFieldType.Enum
    override val isNullable: Boolean = false
    override val naturalDefault get() = entries.first()

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): E? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.int(fieldNumber, value.ordinal)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.int(fieldNumber)
        schematic.schematicValues[name] = entries[value]
    }

    infix fun default(value: E?): EnumSchemaField<E> {
        this.definitionDefault = value
        return this
    }

    fun nullable(): NullableEnumSchemaField<E> {
        return NullableEnumSchemaField(entries, definitionDefault)
    }

    override fun encodeToString(schematic: Schematic<*>): String {
        return getValue(schematic).name
    }

}

open class NullableEnumSchemaField<E : Enum<E>>(
    override val entries: Array<E>,
    override var definitionDefault: E?
) : SchemaField<E?>, EnumSchemaFieldCommon<E> {

    override val type: SchemaFieldType get() = SchemaFieldType.Enum
    override val naturalDefault = null
    override var isNullable: Boolean = true

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): E? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.intOrNull(fieldNumber, fieldNumber + 1, value?.ordinal)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.intOrNull(fieldNumber, fieldNumber + 1)
        schematic.schematicValues[name] = value?.let { entries[value] }
    }

    infix fun default(value: E?): NullableEnumSchemaField<E> {
        this.definitionDefault = value
        return this
    }

    fun nullable(): NullableEnumSchemaField<E> {
        return this
    }

    override fun encodeToString(schematic: Schematic<*>): String {
        return requireNotNull(getValue(schematic)).name
    }

}