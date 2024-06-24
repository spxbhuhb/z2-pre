package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.html.gridTemplateColumns
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.browser.material.datepicker.DockedDatePickerSelector
import hu.simplexion.z2.browser.material.datepicker.datePicker
import hu.simplexion.z2.schematic.Schematic


fun Z2.datepickerDemo() =

    div {
        grid(gridGap24) {
            gridTemplateColumns = "min-content min-content min-content"

            DockedDatePickerSelector(this) { _, _ -> }

            datePicker(label = strings.datepicker) { }

            div {
                val test = DatePickerTest()
                field { test.dateField }
                field { test.optionalDateField }
            }
        }
    }

private class DatePickerTest : Schematic<DatePickerTest>() {
    var dateField by localDate()
    var optionalDateField by localDate().nullable()
}