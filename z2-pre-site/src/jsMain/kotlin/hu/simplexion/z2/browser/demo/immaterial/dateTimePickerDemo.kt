package hu.simplexion.z2.browser.demo.immaterial

import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.immaterial.schematic.attach
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.util.hereAndNow


fun Z2.dateTimePickerDemo() =

    div {
        grid(gridGap24) {
            gridTemplateColumns = "min-content min-content"

            val test = DateTimePickerTest().also { it.dateTimeField = hereAndNow() }

            div {
                field { test.dateTimeField }
                field { test.optionalDateTimeField }
            }
            div().attach(test) {
                pre {
                    text { test.dateTimeField }
                    text { "\n" }
                    text { test.optionalDateTimeField }
                }
            }
        }
    }

private class DateTimePickerTest : Schematic<DateTimePickerTest>() {
    var dateTimeField by localDateTime()
    var optionalDateTimeField by localDateTime().nullable()
}