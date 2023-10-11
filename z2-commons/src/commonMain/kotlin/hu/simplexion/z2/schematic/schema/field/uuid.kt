package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicList
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings

interface UuidSchemaFieldCommon<T> {
    var nil : Boolean?

    @Suppress("UNCHECKED_CAST")
    fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): UUID<T>? {
        return when (anyValue) {
            is UUID<*> -> anyValue as UUID<T>
            is String -> UUID(anyValue)
            else -> {
                fails += fail(validationStrings.uuidFail)
                null
            }
        }
    }

    fun validateValueCommon(value: UUID<T>, fails: MutableList<ValidationFailInfo>) {
        if (nil != true && value == UUID.nil<T>()) fails += fail(validationStrings.nilFail)
    }
}

open class UuidSchemaField<T>(
    override var definitionDefault: UUID<T>?,
    override var nil : Boolean?,
    override val validForCreate : Boolean
) : SchemaField<UUID<T>>, UuidSchemaFieldCommon<T> {

    override val type: SchemaFieldType get() = SchemaFieldType.UUID
    override val isNullable: Boolean = false
    override val naturalDefault = UUID.nil<T>()

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): UUID<T>? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: UUID<T>, fails: MutableList<ValidationFailInfo>) {
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.uuid(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.uuid<T>(fieldNumber)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: UUID<T>?): UuidSchemaField<T> {
        this.definitionDefault = value
        return this
    }

    infix fun nil(value: Boolean): UuidSchemaField<T> {
        this.nil = value
        return this
    }

    fun nullable() : NullableUuidSchemaField<T> {
        return NullableUuidSchemaField(definitionDefault, nil)
    }

}

open class NullableUuidSchemaField<T>(
    override var definitionDefault: UUID<T>?,
    override var nil : Boolean?
) : SchemaField<UUID<T>?>, UuidSchemaFieldCommon<T> {

    override val type: SchemaFieldType get() = SchemaFieldType.UUID
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): UUID<T>? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun validateValue(value: UUID<T>?, fails: MutableList<ValidationFailInfo>) {
        if (value == null) return
        validateValueCommon(value, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf())
        builder.uuidOrNull(fieldNumber, fieldNumber + 1, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.uuidOrNull<T>(fieldNumber, fieldNumber + 1)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: UUID<T>?): NullableUuidSchemaField<T> {
        this.definitionDefault = value
        return this
    }

    infix fun nil(value: Boolean): NullableUuidSchemaField<T> {
        this.nil = value
        return this
    }

    fun nullable() : NullableUuidSchemaField<T> {
        return this
    }

}

class UuidListSchemaField<T>(
    definitionDefault: MutableList<UUID<T>>?,
    nil: Boolean?,
) : hu.simplexion.z2.schematic.schema.ListSchemaField<UUID<T>> {

    override var name: String = ""

    override val itemSchemaField = UuidSchemaField<T>(null, nil, false)

    override var definitionDefault = definitionDefault?.let { SchematicList(null, definitionDefault, this) }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.uuidList(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.uuidList<T>(fieldNumber).toMutableList()
        schematic.schematicValues[name] = SchematicList(schematic, value, this)
    }

    infix fun default(value: MutableList<UUID<T>>?): UuidListSchemaField<T> {
        definitionDefault = value?.let { SchematicList(null, it, this) }
        return this
    }

    infix fun nil(value: Boolean): UuidListSchemaField<T> {
        itemSchemaField.nil = value
        return this
    }
}