/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.immaterial.table

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.material.em
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.schematic.schema.SchemaField
import org.w3c.dom.set

open class LevelColumn<T>(
    table: Table<T>,
    labelBuilder: Z2Builder,
    renderer: Z2.(row: T) -> Unit,
    comparator: (T, T) -> Int,
    filter: (T, String) -> Boolean,
    initialSize: String = "1fr",
    exportable: Boolean = true,
    exportHeader: LocalizedText?,
    exportFun: ((T) -> Any?)?,
    schemaField: SchemaField<*>?,
    val openIcon: LocalizedIcon = browserIcons.arrowRight,
    val closeIcon: LocalizedIcon = browserIcons.arrowDropDown
) : TableColumn<T>(
    table, labelBuilder, renderer, comparator, filter, initialSize, exportable, exportHeader, exportFun, schemaField
) {

    override var initialSize = 2.7.em

    override var exportable: Boolean = false

    override fun render(cell: Z2, index: Int, row: T) {
        val tableRow = table.renderData[index]
        val levelState = tableRow.levelState

        with(cell) {
            if (tableRow.level > 0) {
                htmlElement.style.background = "lightgray"
            } else {
                htmlElement.dataset[table.LEVEL_TOGGLE] = "true"

                when (levelState) {
                    TableRowLevelState.Single -> Unit
                    TableRowLevelState.Open -> icon(closeIcon)
                    TableRowLevelState.Closed -> icon(openIcon)
                }
            }
        }
    }

    override fun sort() {
        // FIXME level column sort
    }

}