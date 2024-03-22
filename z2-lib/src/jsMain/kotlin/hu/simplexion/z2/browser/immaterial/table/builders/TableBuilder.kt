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

    open fun title(builder : TitleBuilder<T>.() -> Unit) {
        title = true
        titleBuilder = TitleBuilder<T>().apply(builder)
    }

    open fun column(builder : ColumnBuilder<T>.() -> Unit) : ColumnBuilder<T> {
        return ColumnBuilder<T>().apply(builder).also { columns += it }
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

//    fun Z2.searchBar() =
//        div("table-search-bar-container") {
//            val container = this
//            icon(browserIcons.search, cssClass = "table-search-bar-leading-icon")
//            input("table-search-bar-input", "body-medium", "text-select") {
//                (htmlElement as HTMLInputElement).placeholder = browserStrings.searchHint.toString()
//                onFocus { container.addClass("table-search-bar-active")}
//                onBlur { container.removeClass("table-search-bar-active")}
//            }
//            icon(browserIcons.filter, cssClass = "table-search-bar-trailing-icon")
//        }
}