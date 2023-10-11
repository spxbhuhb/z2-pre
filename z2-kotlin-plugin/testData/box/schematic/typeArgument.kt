package foo.bar

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.runtime.Schematic

class Test : Schematic<Test>() {

    var uuidField by uuid<Test>()

}

fun box(): String {

    val test1 = Test()
    if (test1.uuidField != UUID.nil<Test>()) "Fail"

    val uuid = UUID<Test>()
    val test2 = Test().apply { uuidField = uuid }
    if (test2.uuidField != uuid) "Fail"

    return "OK"
}