package foo.bar

import hu.simplexion.z2.schematic.Schematic

class Test<T> : Schematic<Test<T>>() {

    var genericField by generic<T>()

    var genericFieldOrNull by generic<T?>()

}

fun box(): String {
    val test = Test<Int>()

    test.genericField = 12
    if (test.genericField != 12) return "Fail: genericField != 12"

    test.genericFieldOrNull = 23
    if (test.genericFieldOrNull != 23) return "Fail: genericFieldOrNull != 23"

    test.genericFieldOrNull = null
    if (test.genericFieldOrNull != null) return "Fail: genericFieldOrNull != null"

    return "OK"
}