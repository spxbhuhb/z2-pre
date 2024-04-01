package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.minWidthAuto
import hu.simplexion.z2.browser.css.pl12
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.localization.text.LocalizedText

open class TableActionBuilder<T> {

    var label : LocalizedText = browserStrings._empty
    var handler: () -> Unit = { }
    var hidden: Boolean = false

    infix fun onlyIf(condition: Boolean) {
        hidden = !condition
    }

    open fun Z2.build(table: Table<T>) {
        if (hidden) return

        div(pl12, minWidthAuto) {
            textButton(label) { handler() }
        }
    }
}