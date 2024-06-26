package hu.simplexion.z2.schematic.util

import hu.simplexion.z2.localization.fallbackNamespace
import hu.simplexion.z2.localization.localizedTextStore
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.localization.text.StaticText
import hu.simplexion.z2.localization.traceLocalization
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField

fun SchemaField<*>.label(schematic : Schematic<*>) : LocalizedText {
    val key = "${schematic.schematicFqName}/$name"
    val value = localizedTextStore[key]
    if (value != null) return value

    val fallback = localizedTextStore["$fallbackNamespace/$name"]
    if (fallback != null) return fallback

    if (traceLocalization) println("[WARNING]  missing localization for $key, fallback is $name")
    return StaticText(key, name)
}