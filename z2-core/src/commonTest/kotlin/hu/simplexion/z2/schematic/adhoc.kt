package hu.simplexion.z2.schematic

class Test<T> : Schematic<Test<*>>() {

    var genericField by generic<T>()

}

fun box(): String {
    val test = Test<Int>()

    test.genericField = 12

    if (test.genericField != 12) return "Fail: genericField != 12"

    return "OK"
}