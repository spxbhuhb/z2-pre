package hu.simplexion.z2.schematic.util

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField

class SchematicDifference<T>(
    val field : SchemaField<T>,
    val thisValue : T,
    val otherValue : T
)

fun <T : Schematic<T>> Schematic<T>.diff(other : Schematic<T>) : List<SchematicDifference<*>> {
    val differences = mutableListOf<SchematicDifference<*>>()

    for (field in this.schematicSchema.fields) {
        val thisValue = field.getValue(this)
        val otherValue = field.getValue(other)
        if (thisValue != otherValue) {
            @Suppress("UNCHECKED_CAST")
            differences += SchematicDifference(field as SchemaField<Any?>, thisValue, otherValue)
        }
    }

    return differences
}