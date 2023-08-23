package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoInstant
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

interface InstantSchemaFieldCommon {

     fun toTypedValueCommon(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Instant? {
        return when (anyValue) {
            is Instant -> anyValue
            is String -> Instant.parse(anyValue)
            else -> {
                fails += fail(validationStrings.instantFail)
                null
            }
        }
    }

}

open class InstantSchemaField(
    override var definitionDefault: Instant?
) : SchemaField<Instant>, InstantSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Instant
    override val isNullable: Boolean = false
    override val naturalDefault = Clock.System.now()

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Instant? {
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, ProtoInstant, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, ProtoInstant)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Instant?): InstantSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable() : NullableInstantSchemaField {
        return NullableInstantSchemaField(definitionDefault)
    }

}

open class NullableInstantSchemaField(
    override var definitionDefault: Instant?
) : SchemaField<Instant?>, InstantSchemaFieldCommon {

    override val type: SchemaFieldType get() = SchemaFieldType.Instant
    override val isNullable: Boolean = true
    override val naturalDefault = null

    override var name: String = ""

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Instant? {
        if (anyValue == null) return null
        return toTypedValueCommon(anyValue, fails)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instanceOrNull(fieldNumber, fieldNumber + 1, ProtoInstant, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, ProtoInstant)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Instant?): NullableInstantSchemaField {
        this.definitionDefault = value
        return this
    }

    fun nullable() : NullableInstantSchemaField {
        return this
    }

}