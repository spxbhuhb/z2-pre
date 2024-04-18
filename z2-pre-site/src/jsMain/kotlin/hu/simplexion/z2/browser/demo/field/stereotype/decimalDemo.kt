package hu.simplexion.z2.browser.demo.field.stereotype

import hu.simplexion.z2.browser.css.displayGrid
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.field.stereotype.decimalField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.gridAutoRows
import hu.simplexion.z2.browser.html.gridTemplateColumns
import hu.simplexion.z2.browser.immaterial.schematic.attach
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.localization.locales.toDecimalString
import hu.simplexion.z2.schematic.Schematic

class DecimalData : Schematic<DecimalData>() {
    var value by decimal()
}

fun Z2.decimalDemo() =
    surfaceContainerLow(displayGrid, gridGap24) {
        gridTemplateColumns = "1fr 1fr 1fr 1fr"
        gridAutoRows = "min-content"

        demo(0, 0)
        demo(0, 1)
        demo(0, 2)
        demo(0, 3)
        demo(123, 0)
        demo(456, 1)
        demo(789, 2)
        demo(123, 3)

        div {
            val data = DecimalData()
            div().attach(data) { + "scale = 2  value = ${data.value.toDecimalString(2)}" }
            field { data.value }
        }
    }

private fun Z2.demo(iv: Long, scale: Int) =
    div {
        val data = DecimalData()
        div().attach(data) { + "scale = $scale  value = ${data.value.toDecimalString(scale)}" }
        decimalField(iv, scale, strings.decimal) { data.value = it }
    }