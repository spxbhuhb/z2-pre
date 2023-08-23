package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

class LongSchemaField(
    override val name: String,
    override val nullable: Boolean,
    override val definitionDefault: Long?,
    val min: Long?,
    val max: Long?
) : SchemaField<Long> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Long

    override val naturalDefault = 0L

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Long? {
        if (anyValue == null) return null

        return when (anyValue) {
            is Long -> anyValue
            is Number -> anyValue.toLong()
            is String -> anyValue.toLongOrNull()
            else -> {
                fails += fail(validationStrings.integerFail)
                null
            }
        }
    }

    override fun validateNotNullable(value: Long, fails: MutableList<ValidationFailInfo>) {
        if (min != null && value < min) fails += fail(validationStrings.minValueFail, min)
        if (max != null && value > max) fails += fail(validationStrings.maxValueFail, max)
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.long(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.long(fieldNumber)
        schematic.schematicValues[name] = value
    }

}