package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.immaterial.schematic.State
import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.browser.immaterial.table.TableConfiguration

open class TableBuilder<T>(
    val table : Table<T>
) : TableConfiguration<T>() {

    val columns = mutableListOf<ColumnBuilder<T>>()

    var query: (suspend () -> List<T>)? = null

    var data: List<T>? = null

    var state : State<List<T>>? = null

    var rowId: ((row : T) -> Any)? = null

    open fun header(builder: HeaderBuilder<T>.() -> Unit) {
        header = true
        headerBuilder = { HeaderBuilder<T>().apply(builder).build(table) }
    }

    open fun column(builder : ColumnBuilder<T>.() -> Unit) : ColumnBuilder<T> {
        return ColumnBuilder<T>().apply(builder).also { columns += it }
    }

    open fun levelColumn(builder: LevelColumnBuilder<T>.() -> Unit) {
        columns += LevelColumnBuilder<T>().apply(builder)
    }

    open fun actionColumn(builder : ActionColumnBuilder<T>.() -> Unit) {
        columns += ActionColumnBuilder<T>().apply(builder)
    }

    open fun editActionColumn(actionFun : suspend (row : T) -> Unit) {
        columns += ActionColumnBuilder<T>().apply {
            action {
                label = browserStrings.edit
                handler = actionFun
            }
        }
    }

    open fun detailsActionColumn(actionFun : suspend (row : T) -> Unit) {
        columns += ActionColumnBuilder<T>().apply {
            action {
                label = browserStrings.details
                handler = actionFun
            }
        }
    }

//    fun Z2.titleBar(): Z2 =
//        grid("table-title-bar") {
//            if (!title) return@grid
//
//            grid(alignSelfCenter) {
//                gridTemplateColumns = "min-content min-content"
//                gridTemplateRows = "min-content"
//                gridGap = 16.px
//                div(alignSelfCenter) {
//                    textTitle()
//                }
//                if (add) outlinedIconButton(browserIcons.add, browserStrings.add) { }
//            }
//
//            div(positionRelative) {
//                if (search) {
//                    searchBar?.let { it() }
//                }
//            }
//
//            div(alignSelfCenter) {
//                if (export) outlinedIconButton(browserIcons.export, browserStrings.export) { }
//            }
//        }


}