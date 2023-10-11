package hu.simplexion.z2.exposed

import hu.simplexion.z2.schematic.runtime.Schematic

class TestSchematic : Schematic<TestSchematic>() {

    var uuid by uuid<TestSchematic>()

    var booleanField by boolean()
    var enumField by enum<TestEnum>()
    var intField by int()
    var stringField by string()
    var uuidField by uuid<TestSchematic>()

}