package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.browser.immaterial.table.TableColumn
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.schematic.schema.SchemaField

open class ColumnBuilder<T> {

    var label: LocalizedText = browserStrings._empty
    var labelBuilder: Z2Builder = { text { label } }
    var render: Z2.(row: T) -> Unit = { }
    var comparator: (T, T) -> Int = { _, _ -> 0 }
    var filter: (T, String) -> Boolean = { _, _ -> false }
    var initialSize = "1fr"
    var exportable = true
    var exportHeader: LocalizedText? = null
    var exportFun : ((T) -> Any?)? = null
    var fieldType : SchemaField<*>? = null

    open fun toColumn(table: Table<T>): TableColumn<T> =
        TableColumn(
            table,
            labelBuilder,
            render,
            comparator,
            filter,
            initialSize,
            exportable,
            exportHeader,
            exportFun,
            fieldType
        )

    infix fun label(value: LocalizedText): ColumnBuilder<T> {
        label = value
        return this
    }

    infix fun initialSize(value: String): ColumnBuilder<T> {
        initialSize = value
        return this
    }
}