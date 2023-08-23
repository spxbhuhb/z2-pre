package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoLocalDateTime
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings
import kotlinx.datetime.LocalDateTime

class LocalDateTimeSchemaField(
    override val name: String,
    override val nullable: Boolean,
    override val definitionDefault: LocalDateTime?
) : SchemaField<LocalDateTime> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.LocalDateTime

    override val naturalDefault = LocalDateTime(0,1,1,0,0,0,0)

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDateTime? {
        if (anyValue == null) return null

        return when (anyValue) {
            is LocalDateTime -> anyValue
            is String -> LocalDateTime.parse(anyValue)
            else -> {
                fails += fail(validationStrings.instantFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: LocalDateTime, fails: MutableList<ValidationFailInfo>) {

    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, ProtoLocalDateTime, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, ProtoLocalDateTime)
        schematic.schematicValues[name] = value
    }

}