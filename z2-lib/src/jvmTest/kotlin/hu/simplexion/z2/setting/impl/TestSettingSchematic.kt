package hu.simplexion.z2.setting.impl

import hu.simplexion.z2.schematic.Schematic

class TestSettingSchematic : Schematic<TestSettingSchematic>() {
    var i1 by int()
    var i2 by int(default = 1)
    var s1 by string()
    var s2 by string(default = "a")
}