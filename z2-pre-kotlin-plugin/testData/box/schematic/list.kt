package foo.bar

import hu.simplexion.z2.schematic.Schematic

class Test : Schematic<Test>() {

    var intList by list<Int>()

}

fun box(): String {
    val test = Test()

    test.intList = listOf(1, 2, 3)

    if (test.intList.size != 3) return "Fail: size != 3"
    if (test.intList.first() != 1) return "Fail: first != 1"

    test.intList = emptyList()
    if (test.intList.isNotEmpty()) return "Fail: notEmpty"

    return "OK"
}