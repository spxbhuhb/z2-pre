package hu.simplexion.z2.localization

import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.localization.text.StaticText
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField

@Deprecated("use the one from core instead")
fun SchemaField<*>.label(schematic : Schematic<*>) : LocalizedText {
    val key = "${schematic.schematicFqName}/$name"
    val value = localizedTextStore[key]
    if (value != null) return value

    val fallback = localizedTextStore["$fallbackNamespace/$name"]
    if (fallback != null) return fallback

    if (traceLocalization) println("[WARNING]  missing localization for $key, fallback is $name")
    return StaticText(key, name)
}