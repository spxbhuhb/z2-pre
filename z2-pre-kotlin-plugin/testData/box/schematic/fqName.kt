package foo.bar

import hu.simplexion.z2.schematic.Schematic

class Test : Schematic<Test>() {

    var intField by int(min = 5)

}

fun box(): String {
    val companionFqName = Test.schematicFqName
    if (companionFqName != "foo.bar.Test") return "Fail: companionFqName=$companionFqName"

    val instanceFqName = Test().schematicFqName
    if (instanceFqName != "foo.bar.Test") return "Fail: instanceFqName=$instanceFqName"

    return "OK"
}