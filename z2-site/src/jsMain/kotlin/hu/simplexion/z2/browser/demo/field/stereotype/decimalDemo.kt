package hu.simplexion.z2.browser.demo.field.stereotype

import hu.simplexion.z2.browser.css.displayGrid
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.field.stereotype.decimalField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.gridTemplateColumns
import hu.simplexion.z2.browser.html.gridTemplateRows
import hu.simplexion.z2.browser.layout.surfaceContainerLow

fun Z2.decimalDemo() =
    surfaceContainerLow(displayGrid, gridGap24) {
        gridTemplateColumns = "1fr 1fr"
        gridTemplateRows = "min-content"

        decimalField(0, 0, strings.decimal) {  }
    }