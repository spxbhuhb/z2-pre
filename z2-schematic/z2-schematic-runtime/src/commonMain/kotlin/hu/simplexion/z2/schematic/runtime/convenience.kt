package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.util.PublicApi

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