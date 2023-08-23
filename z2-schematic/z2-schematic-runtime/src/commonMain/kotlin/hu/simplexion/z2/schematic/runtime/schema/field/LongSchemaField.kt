package hu.simplexion.z2.schematic.runtime.schema.field

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

open class LongSchemaField(
    override var definitionDefault: Long?,
    var min: Long?,
    var max: Long?
) : SchemaField<Long> {

    override var name: String = ""
    override var nullable: Boolean = false

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
        min?.let { if (value < it) fails += fail(validationStrings.minValueFail, it) }
        max?.let { if (value > it) fails += fail(validationStrings.maxValueFail, it) }
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        val value = toTypedValue(schematic.schematicValues[name], mutableListOf()) ?: return
        builder.long(fieldNumber, value)
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        val value = message.long(fieldNumber)
        schematic.schematicValues[name] = value
    }

    infix fun default(value: Long?): LongSchemaField {
        this.definitionDefault = value
        return this
    }

    infix fun min(value: Long): LongSchemaField {
        this.min = value
        return this
    }

    infix fun max(value: Long): LongSchemaField {
        this.max = value
        return this
    }

}