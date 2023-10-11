package hu.simplexion.z2.schematic.schema

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.commons.util.PublicApi
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicCompanion
import hu.simplexion.z2.schematic.schema.validation.FieldValidationResult
import hu.simplexion.z2.schematic.schema.validation.SchematicValidationResult

class Schema<ST : Schematic<ST>>(
    val companion : SchematicCompanion<ST>,
    vararg val fields : SchemaField<*>
) {

    /**
     * Get a field by its name.
     */
    fun getField(fieldName : String) = fields.first { it.name == fieldName }

    /**
     * Calls the `validate` function of all fields in the schema on the value
     * that belongs to the field in [schematic].
     */
    fun validate(schematic : Schematic<*>) : SchematicValidationResult {
        var valid = true
        var validForCreate = true

        val fieldResults = mutableMapOf<String,FieldValidationResult>()

        for (field in fields) {
            val fieldResult = field.validate(schematic.schematicValues[field.name])
            valid = valid && fieldResult.valid
            validForCreate = validForCreate && fieldResult.validForCreate
            fieldResults[field.name] = fieldResult
        }

        return SchematicValidationResult(
            valid,
            validForCreate,
            fieldResults
        )
    }

    /**
     * Calls the `validateSuspend` function of all fields in the schema on the value
     * that belongs to the field in [schematic].
     */
    @PublicApi
    suspend fun validateSuspend(schematic : Schematic<*>) : SchematicValidationResult{
        var valid = true
        var validForCreate = true
        val fieldResults = mutableMapOf<String,FieldValidationResult>()

        for (field in fields) {
            val fieldResult = field.validateSuspend(schematic.schematicValues[field.name])
            valid = valid && fieldResult.valid
            validForCreate = validForCreate && fieldResult.validForCreate
            fieldResults[field.name] = fieldResult
        }

        return SchematicValidationResult(
            valid,
            validForCreate,
            fieldResults
        )
    }

    /**
     * Initializes all fields to the default value. These changes do **NOT**
     * go into the change list, nor they generate listener calls. Requires
     * `schematicValues` to be empty/
     *
     * @throws  IllegalStateException  `schematicValues` is not empty
     */
    fun initWithDefaults(schematic: Schematic<*>) {
        check(schematic.schematicValues.isEmpty()) { "initWithDefaults called on a non-empty schematic" }
        for (field in fields) {
            field.initWithDefault(schematic)
        }
    }

    @PublicApi
    fun newInstance() : ST =
        companion.newInstance()

    fun encodeProto(schematic: Schematic<*>) : ByteArray {
        val builder = ProtoMessageBuilder()
        var fieldNumber = 1
        for (field in fields) {
            field.encodeProto(schematic, fieldNumber, builder)
            fieldNumber += if (field.isNullable) 2 else 1
        }
        return builder.pack()
    }

    fun decodeProto(schematic: Schematic<*>, message: ProtoMessage?) : Schematic<*> {
        if (message == null) return schematic
        var fieldNumber = 1
        for (field in fields) {
            field.decodeProto(schematic, fieldNumber, message)
            fieldNumber += if (field.isNullable) 2 else 1
        }
        return schematic
    }

    fun dump(schematic: Schematic<*>, indent : String = "", result : MutableList<String>) : MutableList<String> {
        for (field in fields) {
            result += "${indent}FIELD  name=${field.name}  type=${field.type}  value=${schematic.schematicValues[field.name]}"
        }
        return result
    }
}