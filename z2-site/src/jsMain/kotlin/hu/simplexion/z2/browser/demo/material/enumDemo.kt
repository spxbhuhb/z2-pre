package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.css.borderBottomOutline
import hu.simplexion.z2.browser.css.widthFull
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.schematic.Schematic


fun Z2.enumDemo() =

    div {
        val test = EnumTest()
        field { test.enumField }
        div(widthFull, borderBottomOutline) { }
        field { test.optionalEnumField }
    }

private enum class A {
    EnumValue1,
    EnumValue2,
    EnumValue3
}

private class EnumTest : Schematic<EnumTest>() {
    var enumField by enum<A>()
    var optionalEnumField by enum<A>().nullable()
}