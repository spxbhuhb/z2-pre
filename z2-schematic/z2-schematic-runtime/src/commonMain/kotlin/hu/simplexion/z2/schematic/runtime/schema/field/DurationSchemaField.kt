package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoDuration
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings
import kotlin.time.Duration

class DurationSchemaField(
    override val name: String,
    override val nullable: Boolean,
    override val definitionDefault: Duration?
) : SchemaField<Duration> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Duration

    override val naturalDefault = Duration.ZERO

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Duration? {
        if (anyValue == null) return null

        return when (anyValue) {
            is Duration -> anyValue
            is String -> Duration.parse(anyValue)
            else -> {
                fails += fail(validationStrings.durationFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: Duration, fails: MutableList<ValidationFailInfo>) {

    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, ProtoDuration, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, ProtoDuration)
        schematic.schematicValues[name] = value
    }

}