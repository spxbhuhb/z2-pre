package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

open class UuidSchemaField<T>(
    override var definitionDefault: UUID<T>?,
    var nil : Boolean?
) : SchemaField<UUID<T>> {

    override var name: String = ""
    override var nullable: Boolean = false

    override val type: SchemaFieldType
        get() = SchemaFieldType.UUID

    override val naturalDefault = UUID.nil<T>()

    @Suppress("UNCHECKED_CAST")
    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): UUID<T>? {
        if (anyValue == null) return null

        return when (anyValue) {
            is UUID<*> -> anyValue as UUID<T>
            is String -> UUID(anyValue)
            else -> {
                fails += fail(validationStrings.uuidFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: UUID<T>, fails: MutableList<ValidationFailInfo>) {
        if (nil != true && value == UUID.nil<T>()) fails += fail(validationStrings.nilFail)
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

}