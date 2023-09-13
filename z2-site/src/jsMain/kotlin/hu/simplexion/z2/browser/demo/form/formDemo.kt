package hu.simplexion.z2.browser.demo.form

import hu.simplexion.z2.browser.components.schematic.attach
import hu.simplexion.z2.browser.components.schematic.field
import hu.simplexion.z2.browser.components.schematic.radioField
import hu.simplexion.z2.browser.css.displayGrid
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.overflowXAuto
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.button.filledButton
import hu.simplexion.z2.commons.util.hereAndNow
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.dump
import kotlinx.datetime.LocalDate

enum class TestEnum {
    EnumValue1,
    EnumValue2,
    EnumValue3
}

val radioItems = listOf("Item 1", "Item 2", "Item 3")

class TestData : Schematic<TestData>() {
    var stringField by string() blank false
    var localDateField by localDate()
    var enumField by enum<TestEnum>()
    var radioSelect by string()
}

fun Z2.formDemo() =
    surfaceContainerLow(displayGrid, gridGap24) {
        gridTemplateColumns = "1fr 1fr"
        gridTemplateRows = "min-content"

        val data = TestData().apply {
            stringField = "Hello World"
            localDateField = LocalDate(2000, 1, 1)
        }

        div {
            field { data.stringField }
            field { data.localDateField }
            field { data.enumField }
            radioField(radioItems) { data.radioSelect }
        }

        div(overflowXAuto) {
            filledButton(strings.setProgrammatically) {
                data.stringField = "Programmatically set at ${hereAndNow()}"
                data.localDateField = LocalDate(1999, 9, 9)
                data.enumField = TestEnum.EnumValue3
            }

            val dump = pre { }

            attach(data) {
                dump.clear()
                dump.htmlElement.innerText = "${hereAndNow()}\n\n" + data.dump()
            }
        }
    }