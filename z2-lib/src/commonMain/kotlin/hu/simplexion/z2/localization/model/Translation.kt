package hu.simplexion.z2.localization.model

import hu.simplexion.z2.schematic.Schematic

class Translation : Schematic<Translation>() {
    var locale by uuid<Locale>()
    var key by string(blank = false)
    var value by string()
    var verified by boolean()
}