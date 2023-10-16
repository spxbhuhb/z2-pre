import hu.simplexion.z2.localization.LocalizationProvider
import hu.simplexion.z2.localization.text.LocalizedTextProvider

lateinit var testVal : ITest2

object test1 : ITest2 {
    val value1 = static("test-value-1")
}

interface ITest2 : LocalizedTextProvider {
    val value2 get() = static("test-value-2")
    val value3 get() = static("test-value-3")
}

enum class Test3 : LocalizationProvider {
    EnumValue1,
    EnumValue2
}

class Test4 : LocalizationProvider {
    val hello = ""
}

fun box() : String {
    if (test1.value1.key != "test1/value1") return "Fail key 1"
    if (test1.value1.value != "test-value-1") return "Fail value 1"

    if (test1.value2.key != "test1/value2") return "Fail key 2"
    if (test1.value2.value != "test-value-2") return "Fail value 2"

    if (test1.value3.key != "test1/value3") return "Fail key 3"
    if (test1.value3.value != "test-value-3") return "Fail value 3"

    testVal = test1

    if (testVal.value3.key != "test1/value3") return "Fail testVal.key 3"
    if (testVal.value3.value != "test-value-3") return "Fail testVal.value 3"

    return "OK"
}