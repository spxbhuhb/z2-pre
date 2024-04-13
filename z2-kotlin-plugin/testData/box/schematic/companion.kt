package foo.bar

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicCompanion

class NoCompanion : Schematic<NoCompanion>() {

    var intField by int(min = 5)

}

class NoSupertype : Schematic<NoSupertype>() {

    var intField by int(min = 5)

    companion object {
        val a = 12
    }
}

class CompanionWithSupertype : Schematic<CompanionWithSupertype>() {

    var intField by int(min = 5)

    companion object : SchematicCompanion<CompanionWithSupertype> {
        val a = 23
    }
}


fun box(): String {
    if (NoCompanion.schematicFqName != "foo.bar.NoCompanion") return "Fail: NoCompanion.schematicFqName != \"foo.bar.NoCompanion\""

    if (NoSupertype.schematicFqName != "foo.bar.NoSupertype") return "Fail: NoSupertype.schematicFqName != \"foo.bar.NoSupertype\""
    if (NoSupertype.a != 12) return "Fail: NoSupertype.a != 12"

    if (CompanionWithSupertype.schematicFqName != "foo.bar.CompanionWithSupertype") return "Fail: CompanionWithSupertype.schematicFqName != \"foo.bar.CompanionWithSupertype\".schematicFqName != \"foo.bar.CompanionWithSupertype.schematicFqName != \"foo.bar.CompanionWithSupertype\"\""
    if (CompanionWithSupertype.a != 23) return "Fail: CompanionWithSupertype.a != 23"

    return "OK"
}