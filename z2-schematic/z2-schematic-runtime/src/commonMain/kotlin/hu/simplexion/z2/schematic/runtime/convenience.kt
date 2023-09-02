package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.util.PublicApi
import hu.simplexion.z2.schematic.runtime.schema.validation.FieldValidationResult
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import hu.simplexion.z2.schematic.runtime.schema.validation.fail

@PublicApi
fun Schematic<*>.dump(separator: String = "\n"): String =
    schematicSchema.dump(this, "", mutableListOf()).joinToString(separator)

/**
 * Ensures that the schematic is valid according to the schema.
 *
 * @param  forCreate  When true fields that allow invalid values in create mode
 *                    are accepted no matter the content.
 */
fun ensureValid(schematic: Schematic<*>, forCreate: Boolean = false) {
    val report = schematic.schematicSchema.validate(schematic)

    if (report.valid) return
    if (forCreate && report.validForCreate) return

    throw IllegalArgumentException(report.fieldResults.filter { ! it.value.valid }.toString())
}

/**
 * Make a [FieldValidationResult] that is valid when `this` Boolean is valid.
 * When invalid, fails contains one entry with [reason].
 */
fun Boolean.asFieldValidationResult(reason : LocalizedText) : FieldValidationResult {
    val fails = mutableListOf<ValidationFailInfo>()
    if (!this) fails += fail(reason)
    return FieldValidationResult("", this, this, fails)
}
