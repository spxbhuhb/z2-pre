package hu.simplexion.z2.schematic.runtime.schema

import hu.simplexion.z2.schematic.runtime.SchematicList
import hu.simplexion.z2.schematic.runtime.schema.validation.FieldValidationResult
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail
import hu.simplexion.z2.schematic.runtime.schema.validation.validationStrings

interface ListSchemaField<VT> : SchemaField<List<VT>> {

    override val type: SchemaFieldType
        get() = SchemaFieldType.List

    override val isNullable: Boolean
        get() = false

    override val naturalDefault: SchematicList<VT>
        get() = SchematicList(null, mutableListOf(), this)

    val itemSchemaField: SchemaField<VT>

    override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): SchematicList<VT>? {
        when (anyValue) {
            null -> {
                fails += fail(validationStrings.nullFail)
                return null
            }

            is SchematicList<*> -> {
                @Suppress("UNCHECKED_CAST") // TODO think about schematic list assignment
                return SchematicList(null, anyValue.backingList as MutableList<VT>, this)
            }

            is Collection<*> -> {
                @Suppress("UNCHECKED_CAST") // TODO think about schematic list assignment
                return SchematicList(null, anyValue.toMutableList() as MutableList<VT>, this)
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
            fails
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun copy(value: Any?): List<VT>? {
        if (value == null) return null

        value as SchematicList<VT>

        return SchematicList(
            null,
            value.map { itemSchemaField.copy(it) }.toMutableList() as MutableList<VT>,
            this
        )
    }
}