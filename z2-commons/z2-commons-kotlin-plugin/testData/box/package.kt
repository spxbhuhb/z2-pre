package a.b.c

import hu.simplexion.z2.commons.localization.LocalizationProvider
import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider

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

fun box() : String {
    if (test1.value1.key != "direct//value1") return "Fail key 1"
    if (test1.value1.value != "test-value-1") return "Fail value 1"

    if (test1.value2.key != "direct//value2") return "Fail key 2"
    if (test1.value2.value != "test-value-2") return "Fail value 2"

    return "OK"
}