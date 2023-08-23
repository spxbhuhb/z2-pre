package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoLocalDate
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings
import kotlinx.datetime.LocalDate

class LocalDateSchemaField(
    override val name: String,
    override val nullable: Boolean,
    override val definitionDefault: LocalDate?
) : SchemaField<LocalDate> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.LocalDate

    override val naturalDefault = LocalDate.fromEpochDays(0)

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): LocalDate? {
        if (anyValue == null) return null

        return when (anyValue) {
            is LocalDate -> anyValue
            is String -> LocalDate.parse(anyValue)
            else -> {
                fails += fail(validationStrings.localDateFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: LocalDate, fails: MutableList<ValidationFailInfo>) {

    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.instance(fieldNumber, ProtoLocalDate, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.instance(fieldNumber, ProtoLocalDate)
        schematic.schematicValues[name] = value
    }

}