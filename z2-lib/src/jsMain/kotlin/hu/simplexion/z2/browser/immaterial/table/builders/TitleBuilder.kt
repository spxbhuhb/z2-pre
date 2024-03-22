package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.adaptive.browser.CssClass
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.text.LocalizedText

class TitleBuilder<T> {

    var text: LocalizedText = browserStrings._empty

    var textClasses = emptyArray<CssClass>()

    val actions = mutableListOf<TableActionBuilder<T>>()

    fun action(label: LocalizedText, handler: () -> Unit) {
        actions += TableActionBuilder<T>().apply {
            this.label = label
            this.handler = handler
        }
    }

    fun build(table: Table<T>): Z2 =
        with(table) {
            grid("1fr min-content", "48px") {
                style.minHeight = 48.px

                div(alignSelfCenter, titleLarge, whiteSpaceNoWrap, pb12, pl8, *textClasses) {
                    text { text.localeCapitalized }
                }

                grid(alignSelfCenter, pb12) {
                    for (action in actions) {
                        textButton(action.label) { action.handler() }
                    }
                }
            }
        }
}