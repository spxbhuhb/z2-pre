package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.adaptive.field.text.impl.ChipTextImpl
import hu.simplexion.z2.adaptive.field.text.impl.textField
import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.alignSelfCenter
import hu.simplexion.z2.browser.css.heightMinContent
import hu.simplexion.z2.browser.css.minWidth208
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.immaterial.table.Table

class TableSearchBuilder<T> : TableActionBuilder<T>() {

    override fun Z2.build(table: Table<T>) {
        div(minWidth208, alignSelfCenter, heightMinContent) {
            textField {
                fieldConfig.impl = ChipTextImpl.uuid
                fieldConfig.trailingIcon = browserIcons.search
                fieldConfig.supportEnabled = false
            } attach {
                table.onSearch(it)
            }
        }
    }

}