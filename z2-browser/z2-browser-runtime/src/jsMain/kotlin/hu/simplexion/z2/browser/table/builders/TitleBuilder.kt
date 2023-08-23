package hu.simplexion.z2.browser.table.builders

import hu.simplexion.z2.browser.css.alignSelfCenter
import hu.simplexion.z2.browser.css.gridGap16
import hu.simplexion.z2.browser.css.titleLarge
import hu.simplexion.z2.browser.css.whiteSpaceNoWrap
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.table.Table
import hu.simplexion.z2.commons.i18n.LocalizedText

class TitleBuilder<T> {

    var text: LocalizedText = basicStrings.EMPTY

    val actions = mutableListOf<TableActionBuilder<T>>()

    fun action(label: LocalizedText, handler: () -> Unit) {
        actions += TableActionBuilder<T>().apply {
            this.label = label
            this.handler = handler
        }
    }

    fun build(table: Table<T>): Z2 =
        with(table) {
            grid {
                gridTemplateRows = "60px"
                gridTemplateColumns = "1fr min-content"

                grid(gridGap16) {
                    gridTemplateColumns = "min-content min-content"
                    gridTemplateRows = "min-content"

                    div(alignSelfCenter, titleLarge, whiteSpaceNoWrap) {
                        text { text }
                    }
                }

                grid {
                    for (action in actions) {
                        textButton(action.label) { action.handler() }
                    }
                }
            }
        }
}