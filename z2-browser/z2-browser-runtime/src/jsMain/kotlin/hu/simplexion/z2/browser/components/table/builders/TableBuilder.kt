package hu.simplexion.z2.browser.components.table.builders

import hu.simplexion.z2.browser.components.table.TableConfiguration

open class TableBuilder<T> : TableConfiguration<T>() {

    val columns = mutableListOf<ColumnBuilder<T>>()

    var query: (suspend () -> List<T>)? = null

    var data: List<T>? = null

    var rowId: ((row : T) -> Any)? = null

    open fun title(builder : TitleBuilder<T>.() -> Unit) {
        titleBuilder = TitleBuilder<T>().apply(builder)
    }

    open fun column(builder : ColumnBuilder<T>.() -> Unit) {
        columns += ColumnBuilder<T>().apply(builder)
    }

    open fun actionColumn(builder : ActionColumnBuilder<T>.() -> Unit) {
        columns += ActionColumnBuilder<T>().apply(builder)
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
//                if (add) outlinedIconButton(basicIcons.add, basicStrings.add) { }
//            }
//
//            div(positionRelative) {
//                if (search) {
//                    searchBar?.let { it() }
//                }
//            }
//
//            div(alignSelfCenter) {
//                if (export) outlinedIconButton(basicIcons.export, basicStrings.export) { }
//            }
//        }

//    fun Z2.searchBar() =
//        div("table-search-bar-container") {
//            val container = this
//            icon(basicIcons.search, cssClass = "table-search-bar-leading-icon")
//            input("table-search-bar-input", "body-medium", "text-select") {
//                (htmlElement as HTMLInputElement).placeholder = basicStrings.searchHint.toString()
//                onFocus { container.addClass("table-search-bar-active")}
//                onBlur { container.removeClass("table-search-bar-active")}
//            }
//            icon(basicIcons.filter, cssClass = "table-search-bar-trailing-icon")
//        }
}