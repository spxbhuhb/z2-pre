package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.util.PublicApi

@PublicApi
fun Schematic<*>.dump(separator : String = "\n") : String =
    schematicSchema.dump(this, "", mutableListOf()).joinToString(separator)

fun validate(schematic : Schematic<*>) {
    val report = schematic.schematicSchema.validate(schematic)
    if (!report.valid) throw IllegalArgumentException(report.fieldResults.toString())
}