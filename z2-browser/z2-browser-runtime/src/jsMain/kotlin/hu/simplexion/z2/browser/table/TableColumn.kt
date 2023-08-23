/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.table

import hu.simplexion.z2.browser.css.labelMedium
import hu.simplexion.z2.browser.css.selectNone
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.util.uniqueNodeId
import hu.simplexion.z2.commons.i18n.LocalizedText
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.math.max


open class TableColumn<T>(
    val table: Table<T>,
    val labelBuilder: Z2Builder,
    val renderer: Z2.(row: T) -> Unit,
    val comparator : (T,T) -> Int,
    var size : Double = Double.NaN,
    val exportable : Boolean = true,
    val exportHeader : LocalizedText
    ) {
    val id = uniqueNodeId

    lateinit var element : HTMLElement

    open val min = 24.0
    open var max = "1fr"

    lateinit var sortSign: Z2
    var sortAscending = true

    var beingResized = false
    var beenResized = false
    var lastX: Int = 0

    fun Z2.header(configuration: TableConfiguration<T>) =
        th(labelMedium) {
            element = this.htmlElement
            if (configuration.fixHeaderHeight) addClass("table-header-cell-fix-height")
            labelBuilder()
            sortSign = sortSign()
            span("table-resize-handle") {
                onMouseDown(::onResizeMouseDown)
            }
            onClick(::onClick)
        }

    fun Z2.sortSign() =
        div {
            div("table-sort-sign") { }
        }

    /**
     * [Table] calls this method whenever [Table.setData] runs.
     */
    open fun onTableSetData() {

    }

    fun gridTemplate(): String {
        return "minmax(${min}px, $max)"
    }

    open fun render(cell: Z2, index: Int, row: T) {
        cell.renderer(row)
    }

    open fun onClick(event: Event) {
        if (beenResized) {
            beenResized = false
            return
        }

        sortAscending = !sortAscending

        if (!table.isInitialized) return

        sort()

        table.columns.forEach {
            it.sortSign.addClass("hidden")
        }

        sortSign.removeClass("ascending", "descending")
        sortSign.addClass("table-sort-sign-container")
        if (sortAscending) {
            sortSign.addClass("ascending")
        } else {
            sortSign.addClass("descending")
        }

        sortSign.removeClass("hidden")

        table.filter()
        table.render()
    }

    open fun onResizeMouseDown(event: MouseEvent) {
        trace { "[resize]  mouse down  lastX=${event.clientX}" }

        beingResized = true
        beenResized = true
        lastX = event.clientX

        table.columns.forEach {
            if (it.id != id) it.element.addClass("table-other-being-resized")
        }

        element.addClass("table-being-resized")
        table.addClass(selectNone)

        window.addEventListener("mouseup", mouseUpWrapper)
        window.addEventListener("mousemove", mouseMoveWrapper)
    }

    val mouseUpWrapper = { event: Event -> onMouseUp(event) }

    open fun onMouseUp(event: Event) {
        trace { "[resize]  mouse up" }

        beingResized = false
        lastX = 0

        table.columns.forEach {
            it.element.removeClass("table-other-being-resized")
        }

        element.removeClass("table-being-resized")
        table.removeClass(selectNone)

        window.removeEventListener("mouseup", mouseUpWrapper)
        window.removeEventListener("mousemove", mouseMoveWrapper)

        event.preventDefault()
    }

    val mouseMoveWrapper = { event: Event -> onMouseMove(event) }

    open fun onMouseMove(event: Event) {
        trace { "[resize]  mouse move  beingResized=$beingResized" }

        event as MouseEvent
        event.preventDefault() // prevents text select during column resize

        window.requestAnimationFrame {
            if (!beingResized) return@requestAnimationFrame

            val distance = event.clientX - lastX
            lastX = event.clientX

            size = max(min, if (size.isNaN()) element.clientWidth.toDouble() else size + distance)

            trace { "======  column resize  ======"}
            trace { "[resize]  id=$id  clientX=${event.clientX}  distance=$distance  size=$size" }

            val tableWidth = table.tableElement.clientWidth
            var sumWidth = 0.0

            table.columns.forEach {
                if (it.size.isNaN()) {
                    it.size = it.element.clientWidth.toDouble()
                }
                sumWidth += it.size
                trace { "[resize.forEach]  id=$id  size=$size  sumWidth=$sumWidth" }
            }

            // When the table is smaller than the sum of colum widths, use 1 fraction for the
            // last column. When larger, use exact pixel widths for each column.

            val template = if (sumWidth >= tableWidth || table.columns.isEmpty()) {
                table.columns.joinToString(" ") { "${it.size}px" }
            } else {
                table.columns.subList(0, table.columns.size - 1).joinToString(" ") { "${it.size}px" } + " 1fr"
            }

            trace { "[resize.apply] sumWidth=$sumWidth template=$template" }

            table.tableElement.style.width = sumWidth.px
            table.tableElement.style.setProperty("grid-template-columns", template)

            table.fullData.forEach { it.height = null }
        }
    }

    fun trace(block : () -> String) {
        if (table.traceColumnResize) println(block())
    }

    protected fun findParentOffset(): Int {
        var offset = 0
        var current = element.offsetParent as? HTMLElement
        while (current != null) {
            if (current.offsetLeft != 0) offset += current.offsetLeft
            current = current.offsetParent as? HTMLElement
        }
        return offset
    }

    /**
     * Converts the cell value into a string.
     */
    open fun format(row: T): String {
        return ""
    }

    /**
     * Sorts the table data by this column.
     */
    open fun sort() {
        table.sort(sortAscending, comparator)
    }

    /**
     * Checks if this column of the given row matches the given string or not.
     */
    open fun matches(row: T, string: String?): Boolean {
        return false
    }

    open fun exportCsvHeader(): String {
        return exportHeader.toString()
    }

    open fun exportCsv(row: T): String {
        return ""
    }

}