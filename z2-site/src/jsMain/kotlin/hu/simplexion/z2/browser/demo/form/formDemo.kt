package hu.simplexion.z2.browser.demo.form

import hu.simplexion.z2.browser.components.schematic.attach
import hu.simplexion.z2.browser.components.schematic.field
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.pre
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.commons.util.hereAndNow
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.dump
import kotlinx.datetime.LocalDate

class TestData : Schematic<TestData>() {
    var stringField by string() blank false
    var localDateField by localDate()
}

fun Z2.formDemo() =
    low {
        val data = TestData().apply {
            stringField = "Hello World"
            localDateField = LocalDate(2000, 1, 1)
        }

        field { data.stringField }
        field { data.localDateField }

        val dump = pre { }

        attach(data) {
            dump.clear()
            dump.htmlElement.innerText = "${hereAndNow()}\n\n" + data.dump()
        }

        textButton(strings.setProgrammatically) {
            data.stringField = "Programmatically set at ${hereAndNow()}"
            data.localDateField = LocalDate(1999, 9, 9)
        }
    }