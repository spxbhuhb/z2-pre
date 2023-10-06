package hu.simplexion.z2.browser.util

import hu.simplexion.z2.commons.localization.localizedTextStore
import hu.simplexion.z2.commons.localization.text.LocalizedText
import hu.simplexion.z2.commons.localization.text.StaticText
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField

fun SchemaField<*>.label(schematic : Schematic<*>) : LocalizedText {
    val key = "schematic/${schematic.schematicFqName}/$name"
    return localizedTextStore[key] ?: StaticText(key, name)
}