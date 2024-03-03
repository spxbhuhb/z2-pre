package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicCompanion
import hu.simplexion.z2.schematic.SchematicList
import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.util.UUID

interface ReferenceSchemaFieldCommon<T : SchematicEntity<T>> {
    var nil : Boolean?

    var companion: SchematicCompanion<T>

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

open class ReferenceSchemaField<T : SchematicEntity<T>>(
    override var definitionDefault: UUID<T>?,
    override var nil : Boolean?,
    override val validForCreate : Boolean
) : SchemaField<UUID<T>>, ReferenceSchemaFieldCommon<T> {

    override val type: SchemaFieldType get() = SchemaFieldType.Reference
    override val isNullable: Boolean = false
    override val naturalDefault = UUID.nil<T>()

    override var name: String = ""

    // set by the compiler plugin
    override lateinit var companion: SchematicCompanion<T>

    // called by the compiler plugin to set the companion
    fun setCompanion(companion: SchematicCompanion<T>) : ReferenceSchemaField<T> {
        this.companion = companion
        return this
    }

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

    infix fun default(value: UUID<T>?): ReferenceSchemaField<T> {
        this.definitionDefault = value
        return this
    }

    infix fun nil(value: Boolean): ReferenceSchemaField<T> {
        this.nil = value
        return this
    }

    fun nullable() : NullableReferenceSchemaField<T> {
        return NullableReferenceSchemaField(definitionDefault, nil)
    }

}

open class NullableReferenceSchemaField<T : SchematicEntity<T>>(
    override var definitionDefault: UUID<T>?,
    override var nil : Boolean?,
) : SchemaField<UUID<T>?>, ReferenceSchemaFieldCommon<T> {

    override val type: SchemaFieldType get() = SchemaFieldType.Reference
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    // set by the compiler plugin
    override lateinit var companion: SchematicCompanion<T>

    // called by the compiler plugin to set the companion
    fun setCompanion(companion: SchematicCompanion<T>) : NullableReferenceSchemaField<T> {
        this.companion = companion
        return this
    }

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

    infix fun default(value: UUID<T>?): NullableReferenceSchemaField<T> {
        this.definitionDefault = value
        return this
    }

    infix fun nil(value: Boolean): NullableReferenceSchemaField<T> {
        this.nil = value
        return this
    }

    fun nullable() : NullableReferenceSchemaField<T> {
        return this
    }

}

class ReferenceListSchemaField<T : SchematicEntity<T>>(
    definitionDefault: MutableList<UUID<T>>?,
    nil: Boolean?
) : ListSchemaField<UUID<T>, ReferenceSchemaField<T>>(
    ReferenceSchemaField(null, nil, false)
) {

    override var name: String = ""

    override var definitionDefault = definitionDefault?.let { SchematicList(null, definitionDefault, this) }

    // called by the compiler plugin to set the companion
    fun setCompanion(companion: SchematicCompanion<T>) : ReferenceListSchemaField<T> {
        itemSchemaField.companion = companion
        return this
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.uuidList(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.uuidList<T>(fieldNumber).toMutableList()
        schematic.schematicValues[name] = SchematicList(schematic, value, this)
    }

    infix fun default(value: MutableList<UUID<T>>?): ReferenceListSchemaField<T> {
        definitionDefault = value?.let { SchematicList(null, it, this) }
        return this
    }

}