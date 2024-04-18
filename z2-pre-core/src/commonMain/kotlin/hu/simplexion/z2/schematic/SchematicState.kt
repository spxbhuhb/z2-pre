package hu.simplexion.z2.schematic

import hu.simplexion.z2.schematic.schema.validation.SchematicValidationResult
import hu.simplexion.z2.util.nextHandle

class SchematicState(
    val schematicNode: SchematicNode
) {

    /**
     * The unique handle of this node. All instances must use
     * `nextHandle` to get their own handle.
     */
    val handle = nextHandle()

    /**
     * The parent of this schematic node if there is one.
     */
    var parent: SchematicNode? = null

    /**
     * When not zero the schematic will create events on data changes
     * and also run validation after each data change. Incremented and
     * decremented by `attach` and `detach` respectively.
     */
    var listenerCount = 0

    private var validationResultOrNull: SchematicValidationResult? = null
        get() {
            if (field == null) {
                require(schematicNode is Schematic<*>) { "schematic list validation is not supported" }
                field = schematicNode.validate()
            }
            return field
        }

    /**
     * Result of the last validation. If there hasn't been a validation before
     * it is run at to get the value of the field.
     */
    var validationResult: SchematicValidationResult
        get() = validationResultOrNull!!
        set(value) {
            validationResultOrNull = value
        }

}