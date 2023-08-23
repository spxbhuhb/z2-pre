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
import kotlinx.datetime.LocalDate

open class InstantSchemaField(
    override var definitionDefault: Instant?
) : SchemaField<Instant> {

    override var name: String = ""
    override var nullable: Boolean = false

    override val type: SchemaFieldType
        get() = SchemaFieldType.Instant

    override val naturalDefault = Clock.System.now()

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Instant? {
        if (anyValue == null) return null

        return when (anyValue) {
            is Instant -> anyValue
            is String -> Instant.parse(anyValue)
            else -> {
                fails += fail(validationStrings.instantFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: Instant, fails: MutableList<ValidationFailInfo>) {

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
}