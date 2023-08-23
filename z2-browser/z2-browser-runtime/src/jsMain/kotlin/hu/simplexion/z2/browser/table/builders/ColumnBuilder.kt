package hu.simplexion.z2.browser.table.builders

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.table.Table
import hu.simplexion.z2.browser.table.TableColumn
import hu.simplexion.z2.commons.i18n.LocalizedText

open class ColumnBuilder<T> {

    var label : LocalizedText = basicStrings.EMPTY
    var labelBuilder : Z2Builder = { text { label } }
    var render: Z2.(row: T) -> Unit = { }
    var comparator: (T,T) -> Int = { _,_ -> 0 }
    var size = Double.NaN
    var exportable = true
    var exportHeader : LocalizedText = basicStrings.EMPTY

    open fun toColumn(table: Table<T>): TableColumn<T> =
        TableColumn(table, labelBuilder, render, comparator, size, exportable, exportHeader)

}
