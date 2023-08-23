package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.datepicker.DatePicker


fun Z2.datepickerDemo() =

    div {
        DatePicker(this)
    }