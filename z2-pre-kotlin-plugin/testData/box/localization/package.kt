package a.b.c

import hu.simplexion.z2.localization.LocalizationProvider
import hu.simplexion.z2.localization.text.LocalizedTextProvider
import hu.simplexion.z2.schematic.Schematic

object test1 : ITest2 {
    val value1 = static("test-value-1")
}

interface ITest2 : LocalizedTextProvider {
    val value2 get() = static("test-value-2")
}

enum class Test3 : LocalizationProvider {
    EnumValue1,
    EnumValue2
}

class Test4 : LocalizationProvider {
    val hello = ""
}

class Test5 : Schematic<Test5>() {
    val intField by int()
}

fun box(): String {
    if (test1.value1.key != "a.b.c.test1/value1") return "Fail key 1"
    if (test1.value1.value != "test-value-1") return "Fail value 1"

    if (test1.value2.key != "a.b.c.test1/value2") return "Fail key 2"
    if (test1.value2.value != "test-value-2") return "Fail value 2"

    if (Test3.EnumValue1.localizationNamespace != "a.b.c.Test3") return "Fail fqname"
    if (Test5().schematicFqName != "a.b.c.Test5") return "Fail fqname"


    return "OK"
}