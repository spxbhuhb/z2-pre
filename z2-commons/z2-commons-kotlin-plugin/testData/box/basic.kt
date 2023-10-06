import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider

object test1 : LocalizedTextProvider {
    val value1 = static("test-value-1")
}

object test2 : LocalizedTextProvider {
    override val namespace = "test-2"
    val value2 = static("test-value-2")
}

fun box() : String {
    if (test1.value1.key != "direct//value1") return "Fail key 1"
    if (test1.value1.value != "test-value-1") return "Fail value 1"

    if (test2.value2.key != "direct/test-2/value2") return "Fail key 2"
    if (test2.value2.value != "test-value-2") return "Fail value 2"

    return "OK"
}