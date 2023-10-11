package hu.simplexion.z2.localization.model

import hu.simplexion.z2.schematic.Schematic

class Translation : Schematic<Translation>() {
    var locale by uuid<Locale>()
    var key by string()
    var value by string()
}