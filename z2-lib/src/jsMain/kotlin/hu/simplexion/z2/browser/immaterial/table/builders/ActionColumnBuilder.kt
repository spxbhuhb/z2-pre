package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.browser.immaterial.table.TableColumn
import hu.simplexion.z2.browser.material.button.textLaunchButton
import hu.simplexion.z2.browser.material.px

class ActionColumnBuilder<T> : ColumnBuilder<T>() {

    val actions = mutableListOf<RowActionBuilder<T>>()

    init {
        label = browserStrings.actions
        filter = { _, _ -> false }
        exportable = false
        render = { actionRender(it, actions) }
    }

    fun action(builder: RowActionBuilder<T>.() -> Unit) {
        actions += RowActionBuilder<T>().apply { builder() }
    }

    override fun toColumn(table: Table<T>): TableColumn<T> {
        return TableColumn(
            table,
            labelBuilder,
            render,
            comparator,
            filter,
            initialSize,
            exportable,
            exportHeader,
            null,
            null
        )
    }

    fun <T> Z2.actionRender(row: T, actions: List<RowActionBuilder<T>>) {

        addCss(
            displayGrid, gridAutoColumnsMinContent, gridAutoFlowColumn, gridGap16, labelMedium
        )

        style.marginLeft = (-12).px

        for (action in actions) {
            textLaunchButton(action.label) { action.handler(row) }
        }

    }

}