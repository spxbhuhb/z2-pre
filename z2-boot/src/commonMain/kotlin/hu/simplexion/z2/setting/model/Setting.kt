package hu.simplexion.z2.setting.model

import hu.simplexion.z2.schematic.Schematic

class Setting : Schematic<Setting>() {
    var path by string() blank false
    var value by string().nullable()
}