package hu.simplexion.z2.browser.immaterial.schematic

import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicEvent
import hu.simplexion.z2.schematic.schema.validation.SchematicValidationResult

/**
 * Sent to the bus of the schematic when the schematic is "touched". This typically
 * means clicking on the submit button of a form. Bound fields do not report errors
 * until they are touched. This prevents the whole form to turn red ad the very
 * beginning. However, when the user clicks on the submit button, all fields have
 * to show their current status.
 */
class Touch(
    override val busHandle: Z2Handle,
    override val schematic: Schematic<*>,
    val validationResult: SchematicValidationResult
) : SchematicEvent

/**
 * Fire a [Touch] event on this schematic.
 *
 * @return  true if the data is valid according to the schema, false otherwise
 */
fun Schematic<*>.touch() : Boolean {
    val vr = this.schematicSchema.validate(this)
    EventCentral.fire(Touch(schematicHandle, this, vr))
    return vr.valid
}