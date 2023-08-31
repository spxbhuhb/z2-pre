package hu.simplexion.z2.browser.demo.form

import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.components.schematic.field
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.pre
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.components.schematic.attach
import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.util.hereAndNow
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.dump

class TestData : Schematic<TestData>() {
    var stringField by string() blank false
}

fun Z2.formDemo() =
    low {
        val data = TestData().apply { stringField = "Hello World" }

        field { data.stringField }

        val dump = pre { }

        attach(data) {
            dump.clear()
            dump.htmlElement.innerText = "${hereAndNow()}\n\n" + data.dump()
        }

        textButton(strings.setProgrammatically) {
            data.stringField = "Programmatically set at ${hereAndNow()}"
        }
    }