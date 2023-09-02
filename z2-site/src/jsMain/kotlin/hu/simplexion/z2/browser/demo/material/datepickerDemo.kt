package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.html.gridTemplateColumns
import hu.simplexion.z2.browser.material.datepicker.DockedDatePicker
import hu.simplexion.z2.browser.material.datepicker.DockedDatePickerSelector


fun Z2.datepickerDemo() =

    div {
        grid(gridGap24) {
            gridTemplateColumns = "min-content min-content"
            DockedDatePickerSelector(this) {  }
            DockedDatePicker(this, label = strings.datepicker) {  }
        }
    }