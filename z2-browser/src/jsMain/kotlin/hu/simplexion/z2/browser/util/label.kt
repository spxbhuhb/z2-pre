package hu.simplexion.z2.browser.util

import hu.simplexion.z2.localization.localizedTextStore
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.localization.text.StaticText
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField

fun SchemaField<*>.label(schematic : Schematic<*>) : LocalizedText {
    val key = "schematic/${schematic.schematicFqName}/$name"
    return localizedTextStore[key] ?: StaticText(key, name)
}