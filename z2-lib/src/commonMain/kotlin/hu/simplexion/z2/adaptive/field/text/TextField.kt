package hu.simplexion.z2.adaptive.field.text

import hu.simplexion.z2.adaptive.field.AdaptiveField
import hu.simplexion.z2.adaptive.field.FieldConfig
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.adaptive.field.FieldValue
import hu.simplexion.z2.schematic.Schematic

class TextField : Schematic<TextField>(), AdaptiveField<String> {
    override val fieldValue by schematic<FieldValue<String>>()
    override val fieldState by schematic<FieldState>()
    override val fieldConfig by schematic<FieldConfig>()
}