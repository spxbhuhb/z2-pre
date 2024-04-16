package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.deprecated.browser.CssClass
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.text.LocalizedText

class HeaderBuilder<T> {

    var text: LocalizedText = browserStrings._empty

    var textClasses = emptyArray<CssClass>()

    var titleBuilder: Z2.() -> Unit = { buildTitle() }

    var actionsBuilder: Z2.(table: Table<T>) -> Unit = { buildActions(it) }

    val actions = mutableListOf<TableActionBuilder<T>>()

    fun title(builder: Z2.() -> Unit) {
        titleBuilder = builder
    }

    fun search() {
        actions += TableSearchBuilder()
    }

    fun action(label: LocalizedText, handler: () -> Unit): TableActionBuilder<T> =
        TableActionBuilder<T>().also {
            it.label = label
            it.handler = handler
            actions += it
        }

    fun build(table: Table<T>): Z2 =
        table.grid("1fr min-content", "48px", minHeight48) {
            titleBuilder()
            actionsBuilder(table)
        }

    fun Z2.buildTitle() {
        div(alignSelfCenter, titleLarge, whiteSpaceNoWrap, pb12, pl8, *textClasses) {
            text { text.localeCapitalized }
        }
    }

    fun Z2.buildActions(table: Table<T>) {
        div(alignSelfCenter, pb12, displayFlex, flexDirectionRow) {
            for (action in actions) {
                with(action) { build(table) }
            }
        }
    }
}