package hu.simplexion.z2.adaptive.field.text

import hu.simplexion.z2.schematic.Schematic

class TextConfig : Schematic<TextConfig>() {

    var renderer by generic<TextRenderer>()

}