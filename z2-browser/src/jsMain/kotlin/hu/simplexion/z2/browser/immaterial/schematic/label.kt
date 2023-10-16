package hu.simplexion.z2.browser.immaterial.schematic

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
    if (traceLocalization) println("[WARNING]  missing localization for $key, fallback is $name")
    return StaticText(key, name)
}