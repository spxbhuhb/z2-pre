package hu.simplexion.z2.schematic.schema.field

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicList
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.validation.FieldValidationResult
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.schema.validation.fail
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.ProtoMessageBuilder

open class ListSchemaField<VT, FT : SchemaField<*>>(
    val itemSchemaField: FT
) : SchemaField<List<VT>> {

    override var name: String = ""

    override val type: SchemaFieldType
        get() = SchemaFieldType.List

    override val isNullable: Boolean
        get() = false

    override val definitionDefault : SchematicList<VT>?
        get() = naturalDefault

    override val naturalDefault: SchematicList<VT>
        get() = SchematicList(mutableListOf(), this)

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): SchematicList<VT>? {
        when (anyValue) {
            null -> {
                fails += fail(validationStrings.nullFail)
                return null
            }

            is SchematicList<*> -> {
                @Suppress("UNCHECKED_CAST") // TODO think about schematic list assignment
                return SchematicList(anyValue.backingList as MutableList<VT>, this)
            }

            is Collection<*> -> {
                @Suppress("UNCHECKED_CAST") // TODO think about schematic list assignment
                return SchematicList(anyValue.toMutableList() as MutableList<VT>, this)
            }

            else -> {
                fails += fail(validationStrings.listFail)
                return null
            }
        }
    }

    override fun validate(anyValue: Any?): FieldValidationResult {
        val fails = mutableListOf<ValidationFailInfo>()

        when (anyValue) {
            null -> fails += fail(validationStrings.nullFail)
            !is MutableList<*> -> fails += fail(validationStrings.listFail)
            else -> {
                for (item in anyValue.listIterator()) {
                    val itemResult = itemSchemaField.validate(item)
                    if (! itemResult.valid) fails += itemResult.fails
                }
            }
        }

        return FieldValidationResult(
            name,
            fails.isEmpty(),
            fails.isEmpty(),
            fails
        )
    }

    override fun encodeProto(schematic: Schematic<*>, fieldNumber: Int, builder: ProtoMessageBuilder) {
        TODO("Not yet implemented")
    }

    override fun decodeProto(schematic: Schematic<*>, fieldNumber: Int, message: ProtoMessage) {
        TODO("Not yet implemented")
    }

    @Suppress("UNCHECKED_CAST")
    override fun copy(value: Any?): SchematicList<VT>? {
        if (value == null) return null

        value as SchematicList<VT>

        return SchematicList(
            value.map { itemSchemaField.copy(it) }.toMutableList() as MutableList<VT>,
            this
        )
    }
}