package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.util.placeholder

/**
 * Generic field type for list.
 */
@Suppress("RedundantNullableReturnType")
class GenericSchemaField<T> : SchemaField<T> {

    override var name: String = ""

    override val type: SchemaFieldType
        get() = SchemaFieldType.Generic

    override val isNullable: Boolean = false

    override val definitionDefault: T?
        get() = null

    override val naturalDefault: T
        get() = placeholder()

    override fun initWithDefault(schematic: Schematic<*>) {
        // generic fields have no natural default TODO think about generic natural default
    }

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): T? {
        @Suppress("UNCHECKED_CAST") // not much to do here TODO better toTypedValue for generic fields
        return anyValue as T?
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) = placeholder()
    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) = placeholder()

}