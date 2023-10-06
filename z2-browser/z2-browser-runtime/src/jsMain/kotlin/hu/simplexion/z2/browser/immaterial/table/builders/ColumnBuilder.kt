package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.browser.immaterial.table.TableColumn
import hu.simplexion.z2.commons.localization.text.LocalizedText

open class ColumnBuilder<T> {

    var label : LocalizedText = browserStrings._empty
    var labelBuilder : Z2Builder = { text { label } }
    var render: Z2.(row: T) -> Unit = { }
    var comparator: (T,T) -> Int = { _,_ -> 0 }
    var initialSize = "1fr"
    var exportable = true
    var exportHeader : LocalizedText = browserStrings._empty

    open fun toColumn(table: Table<T>): TableColumn<T> =
        TableColumn(table, labelBuilder, render, comparator, initialSize, exportable, exportHeader)

}
