package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.browser.immaterial.table.LevelColumn
import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.browser.immaterial.table.TableColumn

open class LevelColumnBuilder<T> : ColumnBuilder<T>() {

    override fun toColumn(table: Table<T>): TableColumn<T> =
        LevelColumn(
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

}