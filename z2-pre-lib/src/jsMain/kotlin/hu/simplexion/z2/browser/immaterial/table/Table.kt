/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.browser.immaterial.table

import hu.simplexion.z2.browser.css.borderNone
import hu.simplexion.z2.browser.css.css
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.css.selectNone
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.immaterial.schematic.State
import hu.simplexion.z2.browser.immaterial.table.builders.TableBuilder
import hu.simplexion.z2.browser.util.downloadCsv
import hu.simplexion.z2.browser.util.getDatasetEntry
import hu.simplexion.z2.deprecated.event.AnonymousEventListener
import hu.simplexion.z2.deprecated.event.EventCentral
import hu.simplexion.z2.util.localLaunch
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.datetime.*
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class Table<T>(
    parent: Z2? = null,
    builder: (table: Table<T>) -> TableBuilder<T>,
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    tableOuterContainerStyles,
    null
) {

    val configuration: TableConfiguration<T>
    val columns: List<TableColumn<T>>

    var query: (suspend () -> List<T>)? = null
    var state: State<List<T>>? = null

    val exportFileName: String
        get() {
            // FIXME val prefix = (titleText ?: this::class.simpleName?.localized ?: "").ifBlank { "content" }
            val prefix = "content"
            val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return "${prefix}_$time".replace("\\W".toRegex(), "_") + ".csv"
        }

    // open var counterBar = ZkCounterBar("")

    var traceScroll = false
    var traceColumnResize = false
    var traceMultiLevel = false
    var traceMatch = false

    var firstOnResume = true

    val rowHeight
        get() = configuration.rowHeight

    // -------------------------------------------------------------------------
    //  DOM
    // -------------------------------------------------------------------------

    lateinit var contentContainer: Z2
    lateinit var table: Z2
    lateinit var tableBody: Z2

    lateinit var contentElement: HTMLElement
    lateinit var tableElement: HTMLElement
    lateinit var tableBodyElement: HTMLElement

    var contentScrollTop: Double = 0.0
    var contentScrollLeft: Double = 0.0

    // Reference: http://www.html5rocks.com/en/tutorials/speed/animations/
    var lastKnownScrollPosition = 0.0
    var ticking = false

    var firstAttachedRowIndex = 0
    var attachedRowCount = 0

    /**
     * Get a unique if for the given row. The id of the row is used by actions and is
     * passed to row based functions such as [onDblClick].
     */
    var getRowId: (row: T) -> Any = {
        throw NotImplementedError("please override ${this::class}.getRowId when not using crud")
    }

    /** HTMLElement.dataset key to store the ID of the row, never changes, calculated by [getRowId]  */
    val ROW_ID = "z2rid"

    /** HTMLElement.dataset key to store the index of the row, the index shows the row index in [renderData] */
    val ROW_INDEX = "z2ridx"

    /** HTMLElement.dataset key to indicte that this is a level toggle for a multi-level row */
    val LEVEL_TOGGLE = "z2lt"

    val addAboveCount: Int
        get() = window.outerHeight / rowHeight

    val addBelowCount: Int
        get() = window.outerHeight / rowHeight

    // -------------------------------------------------------------------------
    //  State -- data of the table, search text, preloaded data
    // -------------------------------------------------------------------------

    val isInitialized
        get() = ::fullData.isInitialized

    /** All the data behind the table (visible and hidden together). */
    lateinit var fullData: List<TableRow<T>>

    /** Filtered [fullData] (if there is a filter, equals to [fullData] otherwise).
     *  contains all child rows in case of multi-level table.
     */
    lateinit var filteredData: List<TableRow<T>>

    /** The data to show to the user, filtered and may be closed (for multi-level tables). */
    lateinit var renderData: MutableList<TableRow<T>>

    // FIXME val preloads = mutableListOf<ZkTablePreload<*>>()

    var searchText: String? = null

    init {
        builder(this).also { tableBuilder ->
            configuration = tableBuilder
            columns = tableBuilder.columns.map { it.toColumn(this) }
            query = tableBuilder.query
            state = tableBuilder.state
            getRowId = checkNotNull(tableBuilder.rowId) { "rowId has to be set in the table builder" }
            tableBuilder.data?.let { setData(it) }
            tableBuilder.state?.let { setData(it.value) }
        }

        configuration.headerBuilder?.invoke(this)

        gridTemplateRows = if (configuration.header) "min-content 1fr" else "1fr"
        gridTemplateColumns = "1fr"

        div("table-content-container".css) {
            contentContainer = this
            contentElement = this.htmlElement

            tableHtml("table".css) {
                tableElement = this.htmlElement
                table = this
                style.cssText = inlineCss()

                thead(selectNone) {
                    for (column in columns) {
                        with(column) { header(configuration) }
                    }
                }

                tbody {
                    tableBody = this
                    tableBodyElement = this.htmlElement
                }
            }

            onScroll(::onScroll)
        }

        onMouseDown(::onMouseDown)
        onDblClick(::onDblClick)
        onClick(::onClick)

        state?.let { s ->
            AnonymousEventListener(s.handle) {
                setData(s.value)
            }.also {
                listeners += it
                EventCentral.attach(it)
            }
        }

        resume()
    }


    fun resume() {

        val query = this.query
        val state = this.state

        when {
            query != null && (configuration.runQueryOnResume || firstOnResume) -> {
                setData(emptyList())
                localLaunch {
                    try {
                        setData(query()) // calls render
                    } catch (ex: Throwable) {
                        // TODO add a function to Application to channel all errors into one place, notify the user, upload the error report, etc.
                        ex.printStackTrace()
                    }
                }
            }

            // this means that setData has been called before onResume
            ::fullData.isInitialized && firstOnResume -> {
                render()
            }
        }

        if (! firstOnResume) {
            window.requestAnimationFrame {
                contentElement.scrollTo(contentScrollLeft, contentScrollTop)
            }
        }

        firstOnResume = false

    }

//    fun titleActions(): List<ZkElement> {
//
//        val actions = mutableListOf<ZkElement>()
//
//        if (add) actions += ZkAddRowAction(::onAddRow)
//        if (export) actions += ZkExportCsvAction(::onExportCsv)
//        if (search) actions += ZkSearchAction(searchText ?: "", ::onSearch)
//
//        return actions
//    }

    fun setCounter() {

//        if (needToSetAllCounter && allCount == null && ::fullData.isInitialized && fullData.isNotEmpty()) {
//            allCount = fullData.size
//            needToSetAllCounter = false
//        }
//
//        val all = allCount ?: ""
//        val count = if (::filteredData.isInitialized) filteredData.size.toString() else all
//
//        counterBar.text = "${localizedStrings.counterTitle} $count/$all"
//        counterBar.onCreate()

    }

    // -------------------------------------------------------------------------
    //  Event Handlers
    // -------------------------------------------------------------------------

    fun getRowId(event: Event): String? {
        if (event !is MouseEvent) return null

        val target = event.target
        if (target !is HTMLElement) return null

        return target.getDatasetEntry(ROW_ID)
    }

    fun onClick(event: Event) {
        val target = event.target

        if (target is HTMLElement && target.getDatasetEntry(LEVEL_TOGGLE) != null) {
            toggleMultiLevelRow(renderData[target.getDatasetEntry(ROW_INDEX) !!.toInt()])
            return
        }

        if (configuration.oneClick) getRowId(event)?.let { onDblClick(it) }
    }

    /**
     * Prevent text selection on double click.
     */
    fun onMouseDown(event: MouseEvent) {
        if (event.detail > 1) {
            event.preventDefault()
        }
    }

    fun onDblClick(event: Event) {
        event.preventDefault()
        getRowId(event)?.let { onDblClick(it) }
    }

    fun onScroll(@Suppress("UNUSED_PARAMETER") event: Event) {
        contentScrollTop = contentElement.scrollTop
        contentScrollLeft = contentElement.scrollLeft

        lastKnownScrollPosition = contentElement.scrollTop.let { if (it < 0) 0.0 else it } // Safari may have negative scrollTop

        if (! ticking) {
            window.requestAnimationFrame {
                onScroll(lastKnownScrollPosition)
                ticking = false
            }
            ticking = true
        }
    }

    // -------------------------------------------------------------------------
    //  Data setter, preload
    // -------------------------------------------------------------------------

    //fun <RT : Any> preload(loader: suspend () -> RT) = ZkTablePreload(loader)

    /**
     * Set data of the table. Asynchronous, waits for the preloads
     * to finish before the data is actually set.
     *
     * @param  data  The data to set.
     */
    fun setData(data: List<T>): Table<T> {
        localLaunch {
            try {
                fullData = data.mapIndexed { index, bo -> TableRow(index, bo) }

                buildMultiLevelState()

                columns.forEach { it.onTableSetData() }

                filter()

                render()
            } catch (ex: Throwable) {
                // TODO add a function to Application to channel all errors into one place, notify the user, upload the error report, etc.
                ex.printStackTrace()
            }
        }
//            preloads.forEach {
//                it.job.join()
//            }
        return this
    }

    /**
     * Builds the states for multi level rows if multi level option is enabled.
     * By default, all top-level rows are closed.
     */
    fun buildMultiLevelState() {
        if (! configuration.multiLevel) return
        if (fullData.isEmpty()) return

        var previousLevel = 0
        check(getRowLevel(fullData[0]) == 0) { "the first row must be level 0" }

        fullData.forEachIndexed { index, row ->

            val level = getRowLevel(row)

            if (previousLevel < level) {
                fullData[index - 1].levelState = TableRowLevelState.Closed
            }

            row.level = level
            previousLevel = level
        }
    }

    /**
     * Execute the given query and set the table data to its
     * result. Asynchronous, waits for the preloads to finish
     * before the data is actually set.
     *
     * Calls the list version of [setData] with the query result.
     *
     * @param  query  The query to execute
     */
    fun setData(query: suspend () -> List<T>) {
        localLaunch {
            try {
                setData(query())
            } catch (ex: Throwable) {
                // TODO add a function to Application to channel all errors into one place, notify the user, upload the error report, etc.
                ex.printStackTrace()
            }
        }
    }

    // -------------------------------------------------------------------------
    //  Rendering, intersection observer callback
    // -------------------------------------------------------------------------

    fun inlineCss() = """
        grid-template-columns: ${columns.joinToString(" ") { it.gridTemplate() }};
    """.trimIndent()

    fun render() {
        firstAttachedRowIndex = 0
        attachedRowCount = min(renderData.size, addBelowCount)

        redraw()
    }

    /**
     * Redraws the currently shown rows of the table.
     * Deletes all cached row renders.
     * Sets scroll position to the latest known value
     */
    fun redraw(): Table<T> {

        for (row in fullData) {
            row.element = null
        }

        window.requestAnimationFrame {

            tableBody.clear()

            addPlaceHolderRow()
            adjustTopPlaceHolder()

            addPlaceHolderRow()
            adjustBottomPlaceHolder()

            attach(firstAttachedRowIndex, attachedRowCount)

            // contentContainer.element.scrollTo(contentScrollLeft, contentScrollTop)

        }

        return this
    }

    /**
     * Adds a placeholder row (top or bottom). These rows change in height so
     * the scrollbar reacts as if all rows would be rendered.
     */
    fun addPlaceHolderRow() {
        tableBody.tr {
            repeat(columns.size) {
                td(borderNone, p0) { }
            }
        }
    }

    /**
     * Renders the row. Caches the result and returns with it for subsequent calls
     * on the same row.
     *
     * @param index Index of the row in state.rowStates
     */
    fun TableRow<T>.render(index: Int, row: TableRow<T>): Z2 {
        element?.let { return it }

        val heightClass = if (configuration.fixRowHeight) "table-fix-height" else "table-variable-height"

        // can't use 'tr' here as it would add the row to the DOM and we don't want that
        element = Z2(
            null,
            document.createElement("tr") as HTMLElement,
            emptyArray()
        ) {
            for (column in columns) {
                td("table-cell".css, heightClass.css) {
                    column.render(this, index, row.data)
                }
            }

            htmlElement.dataset[ROW_ID] = getRowId(row.data).toString()
            htmlElement.dataset[ROW_INDEX] = index.toString()
        }

        return element !!
    }

    // -------------------------------------------------------------------------
    //  Utility functions for scroll
    // -------------------------------------------------------------------------

    /**
     * Attaches a row to the table. Attached rows are not necessarily visible for
     * the user (they may be scrolled out) but they are added to the DOM.
     */
    fun attach(index: Int) {
        // Get the state of the row or create a new one if it doesn't exist yet
        val rowState = renderData[index]

        // Render and attach the row
        rowState.apply {
            render(index, this).also {
                var row = tableBodyElement.firstElementChild as HTMLTableRowElement?
                var added = false
                while (row != null) {
                    val ridx = row.dataset[ROW_INDEX]?.toInt()
                    if (ridx != null && ridx > index) {
                        tableBodyElement.insertBefore(it.htmlElement, row)
                        added = true
                        break
                    }
                    row = row.nextElementSibling as HTMLTableRowElement?
                }

                if (! added) {
                    tableBodyElement.insertBefore(it.htmlElement, tableBodyElement.lastElementChild)
                }
            }
        }
    }

    fun attach(start: Int, count: Int) {
        (start until start + count).forEach {
            attach(it)
        }
    }

    fun remove(index: Int): Double {
        return renderData[index].let { row ->
            row.element?.htmlElement?.remove()
            row.height !!
        }
    }

    fun remove(start: Int, end: Int): Double {
        return (start until end).sumOf { offset -> remove(offset) }
    }

    fun removeAll() {
        val start = firstAttachedRowIndex
        val end = start + attachedRowCount

        (start until end).forEach { index ->
            remove(index)
        }
    }

    fun adjustTopPlaceHolder() {
        var placeHolderCell = tableBodyElement.firstElementChild?.firstElementChild as HTMLTableCellElement?
        val placeHolderHeight = "${rowHeight * firstAttachedRowIndex}px"
        while (placeHolderCell != null) {
            placeHolderCell.style.minHeight = placeHolderHeight
            placeHolderCell.style.height = placeHolderHeight
            placeHolderCell = placeHolderCell.nextElementSibling as HTMLTableCellElement?
        }
    }

    fun adjustBottomPlaceHolder() {
        var placeHolderCell = tableBodyElement.lastElementChild?.firstElementChild as HTMLTableCellElement?
        val placeHolderHeight = if (configuration.noScroll) {
            "0px"
        } else {
            "${rowHeight * (renderData.size - (firstAttachedRowIndex + attachedRowCount))}px"
        }
        while (placeHolderCell != null) {
            placeHolderCell.style.minHeight = placeHolderHeight
            placeHolderCell.style.height = placeHolderHeight
            placeHolderCell = placeHolderCell.nextElementSibling as HTMLTableCellElement?
        }
    }

    /**
     * @return Height of the area that shows rows.
     */
    fun viewHeight(): Double {
        val containerHeight = contentElement.getBoundingClientRect().height
        val headerHeight =
            tableElement.firstElementChild?.firstElementChild?.getBoundingClientRect()?.height ?: rowHeight.toDouble()

        // if (trace) println("contentContainer height: $containerHeight thead height: $headerHeight")

        // TODO subtract footer if needed
        return containerHeight - headerHeight
    }

    /**
     * @return Height of the currently attached rows (in pixels).
     */
    fun attachedHeight(): Double {
        val start = firstAttachedRowIndex
        val end = start + attachedRowCount

        return (start until end).sumOf { index ->
            renderData[index].let { state ->
                state.height ?: state.element !!.htmlElement.firstElementChild !!.getBoundingClientRect().height.also {
                    state.height = it
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    //  Scroll handler
    // -------------------------------------------------------------------------

    /**
     * Calculates the empty area shown on the screen and calls the appropriate function
     * to fill it.
     */
    fun onScroll(scrollTop: Double) {
        val viewHeight = viewHeight() // height of the area that shows rows
        val attachedHeight = attachedHeight()

        val topRowCount = firstAttachedRowIndex

        val topBoundary = rowHeight * topRowCount     // offset to the start of the first rendered row
        val bottomBoundary = topBoundary + attachedHeight      // offset to the end of the last rendered row

        if (traceScroll) println("scrollTop: $scrollTop viewHeight: $viewHeight attachedHeight: $attachedHeight topBoundary: $topBoundary, bottomBoundary: $bottomBoundary")

        when {
            // above attached rows, no attached row on screen
            scrollTop + viewHeight < topBoundary -> fullEmpty(scrollTop, attachedHeight)

            // below attached rows, no attached row on screen
            scrollTop > bottomBoundary -> fullEmpty(scrollTop, attachedHeight)

            // above attached rows, EMPTY area between attached rows and scrollTop
            scrollTop < topBoundary -> partialEmptyAbove(scrollTop, attachedHeight)

            // below attached rows, EMPTY area between attached rows and scrollTop
            scrollTop < bottomBoundary && bottomBoundary < scrollTop + viewHeight -> partialEmptyBelow(scrollTop)

            // there is no empty area
            else -> noEmpty()
        }
    }

    fun fullEmpty(scrollTop: Double, originalAttachedHeight: Double) {
        if (traceScroll) println("fullEmpty")

        removeAll()

        val currentTotalHeight = renderData.size * rowHeight - (attachedRowCount * rowHeight) + originalAttachedHeight
        val scrollPercentage = scrollTop / currentTotalHeight
        firstAttachedRowIndex = floor(renderData.size * scrollPercentage).toInt()
        attachedRowCount = min(renderData.size - firstAttachedRowIndex, addBelowCount)

        if (traceScroll) println("currentTotalHeight: $currentTotalHeight  scrollTop: $scrollTop firstAttachedRowIndex: $firstAttachedRowIndex attachedRowCount: $attachedRowCount")

        attach(firstAttachedRowIndex, attachedRowCount)

        adjustTopPlaceHolder()
        adjustBottomPlaceHolder()
        contentElement.scrollTo(0.0, firstAttachedRowIndex * rowHeight.toDouble())
    }

    fun partialEmptyAbove(scrollTop: Double, originalAttachedHeight: Double) {
        if (traceScroll) println("partialEmptyAbove")

        // Calculate the number of rows to attach, update fields, attach the rows

        val originalFirstAttachedRowIndex = firstAttachedRowIndex
        val originalAttachedRowCount = attachedRowCount

        firstAttachedRowIndex = max(originalFirstAttachedRowIndex - addAboveCount, 0)
        attachedRowCount = originalAttachedRowCount + (originalFirstAttachedRowIndex - firstAttachedRowIndex)

        attach(firstAttachedRowIndex, attachedRowCount - originalAttachedRowCount)

        // Calculate new attached height, so we can adjust the scroll top. This adjustment is
        // necessary because the actual height of the attached rows may be different from
        // the estimated row height. This difference would cause the rows already shown to
        // change position on the screen, we don't want that.

        val newAttachedHeight = attachedHeight()

        val estimatedAddition = (originalFirstAttachedRowIndex - firstAttachedRowIndex) * rowHeight
        val actualAddition = newAttachedHeight - originalAttachedHeight

        contentElement.scrollTop = scrollTop + (actualAddition - estimatedAddition)

        adjustTopPlaceHolder()

        if (traceScroll) println("newAttachedHeight: $newAttachedHeight oldAttachedHeight: $originalAttachedHeight actualAddition : $actualAddition estimatedAddition : $estimatedAddition")

        // Remove rows from the bottom if there are too many attached

        val limit = addBelowCount * 3

        if (attachedRowCount > limit) {

            remove(firstAttachedRowIndex + limit, firstAttachedRowIndex + attachedRowCount)

            attachedRowCount = limit

            adjustBottomPlaceHolder()

            if (traceScroll) println("limit adjustment: start: ${firstAttachedRowIndex + limit} end: ${firstAttachedRowIndex + attachedRowCount} limit : $limit estimatedAddition : $estimatedAddition")
        }
    }

    fun partialEmptyBelow(scrollTop: Double) {
        if (traceScroll) println("partialEmptyBelow")

        // Calculate the number of rows to attach, update fields, attach the rows

        val originalAttachedRowCount = attachedRowCount

        attachedRowCount = min(attachedRowCount + addBelowCount, renderData.size - firstAttachedRowIndex)

        attach(firstAttachedRowIndex + originalAttachedRowCount, attachedRowCount - originalAttachedRowCount)

        adjustBottomPlaceHolder()

        if (traceScroll) println("newAttachedRowCount: $attachedRowCount originalAttachedRowCount: $originalAttachedRowCount")

        // Remove rows from the top if there are too many attached

        val limit = addAboveCount * 3

        if (attachedRowCount > limit) {
            val start = firstAttachedRowIndex
            val end = firstAttachedRowIndex + attachedRowCount - limit

            val actualRemoval = remove(start, end)

            firstAttachedRowIndex = end
            attachedRowCount = limit

            // Calculate new attached height, so we can adjust the scroll top. This adjustment is
            // necessary because the actual height of the removed rows may be different from
            // the estimated row height. This difference would cause the rows still shown to
            // change position on the screen, we don't want that.

            val estimatedRemoval = (end - start) * rowHeight

            if (actualRemoval - estimatedRemoval > 0.5) {
                contentElement.scrollTop = scrollTop - (actualRemoval - estimatedRemoval)
            }

            adjustTopPlaceHolder()

            if (traceScroll) println("limit adjustment: start: $start end: $end limit : $limit estimatedRemoval : $estimatedRemoval actualRemoval: $actualRemoval")
        }
    }

    fun noEmpty() {
        if (traceScroll) println("noEmpty")
    }

    // -------------------------------------------------------------------------
    //  API functions, intended for override
    // -------------------------------------------------------------------------

    /**
     * Get the data of a given row by row ID.
     */
    fun getRowData(id: String): T =
        fullData.first { getRowId(it.data).toString() == id }.data

    /**
     * Set the data of a given row by row ID. Does not update the UI,
     * call [redraw] for that.
     */
    fun setRowData(data: T, optional: Boolean = false) {
        val id = getRowId(data)
        if (optional) {
            fullData.firstOrNull { getRowId(it.data) == id }?.data = data
        } else {
            fullData.first { getRowId(it.data) == id }.data = data
        }
    }

    /**
     * Add a new row to the table.
     */
    fun onAddRow() {

    }

    /**
     * Handle double click on a row.
     *
     * @param  id  Id of the row as given by [getRowId].
     */
    fun onDblClick(id: String) {
        configuration.doubleClickFun?.invoke(getRowData(id))
    }

    /**
     * Performs a search on the table, showing only rows that contain [text].
     *
     * Default implementation:
     *
     * * sets [searchText] to [text]
     * * calls [filter]
     * * calls [render]
     */
    fun onSearch(text: String) {
        searchText = text.ifEmpty { null }
        filter()
        render()
    }

    /**
     * Exports the table to CSV.
     *
     * Default implementation:
     *
     * * exports all the data, not the filtered state, can be changed with [exportFiltered]
     * * calls [ZkColumn.exportCsv] for each row to build the csv line
     * * pops a download in the browser with the file name set to [exportFileName]
     */
    fun onExportCsv() {
        val lines = mutableListOf<String>()

        val data = if (configuration.exportFiltered) filteredData else fullData

        if (configuration.exportHeaders) {
            val fields = mutableListOf<String>()
            columns.forEach { if (it.exportable) fields += it.exportCsvHeader() }
            lines += fields.joinToString(";")
        }

        data.forEach { row ->
            val fields = mutableListOf<String>()
            columns.forEach { if (it.exportable) fields += it.exportCsv(row.data) }
            lines += fields.joinToString(";")
        }

        val csv = lines.joinToString("\n")

        downloadCsv(exportFileName, csv)
    }

    /**
     * Applies all filters on the table rows.
     */
    fun filter() {

        // when there is no filter return with all the data
        // multi-level table needs special processing because of the hidden
        // rows, so check for that also

        if (searchText == null && configuration.filterFun == null && ! configuration.multiLevel) {
            filteredData = fullData
            renderData = filteredData.toMutableList()
            return
        }

        val lc = searchText?.lowercase()

        // when not multi-level, perform single filtering

        if (! configuration.multiLevel) {
            filteredData = fullData.filter { filterRow(it.data, lc) }
            renderData = filteredData.toMutableList()
            return
        }

        // multi-level rows need special filtering:
        // - a top level row matches: add it and add all children
        // - a child row matches: add the top level row and all the children

        val filterResult = mutableListOf<TableRow<T>>()
        val renderResult = mutableListOf<TableRow<T>>()
        var index = 0

        while (index < fullData.size) {
            val row = fullData[index]
            var match = filterRow(row.data, lc) // searchText == null ||

            if (row.levelState == TableRowLevelState.Single) {
                if (traceMultiLevel) println("$index ${row.levelState} ${row.level}")
                if (match) {
                    filterResult += row
                    renderResult += row
                }
                index += 1
                continue
            }

            val children = getChildren(fullData, index, row.level)

            if (! match) match = (children.firstOrNull { filterRow(it.data, lc) } != null)

            if (match) {
                filterResult += row
                filterResult += children
                renderResult += row
                if (traceMultiLevel) println("$index ${row.levelState} ${row.level} ${children.size}")
                if (row.levelState == TableRowLevelState.Open) {
                    renderResult += children
                }
            }

            index += 1 + children.size
        }

        filteredData = filterResult
        renderData = renderResult

        if (traceMultiLevel) println("filter: filteredData.size = ${filteredData.size}  renderData.size = ${renderData.size}")
    }

    /**
     * Applies the filters to one row to decide if that row should be
     * present in the filtered table.
     */
    fun filterRow(row: T, text: String?): Boolean {
        if (configuration.filterFun != null) {
            return configuration.filterFun !!(row)
        } else {
            columns.forEach {
                if (it.matches(row, text)) return true
            }
            return false
        }
    }

    /**
     * Get the level of the row. Override for multi level tables. Level 0
     * means that the row is always shown (if not filtered out).
     */
    fun getRowLevel(row: TableRow<T>): Int {
        return configuration.getRowLevel?.invoke(row.data) ?: 0
    }

    fun toggleMultiLevelRow(row: TableRow<T>) {
        if (row.levelState == TableRowLevelState.Closed) {
            openMultiLevelRow(row)
        } else {
            closeMultiLevelRow(row)
        }
    }

    /**
     * When opening a multi-level row, we have to add the child rows from [filteredData]
     * to [renderData].
     */
    fun openMultiLevelRow(row: TableRow<T>) {

        if (traceMultiLevel) println("openMultiLevel ${row.index}")

        val renderIndex = renderData.indexOfFirst { it.index == row.index }
        val children = getChildren(filteredData, filteredData.indexOf(row), row.level)

        renderData.addAll(renderIndex + 1, children)

        row.levelState = TableRowLevelState.Open

        attachedRowCount += children.size

        redraw() // FIXME this may not cover the whole area - maybe
    }

    fun closeMultiLevelRow(row: TableRow<T>) {

        if (traceMultiLevel) println("closeMultiLevel ${row.index}")

        val renderIndex = renderData.indexOfFirst { it.index == row.index }
        val children = getChildren(filteredData, filteredData.indexOf(row), row.level)

        repeat(children.size) {
            renderData.removeAt(renderIndex + 1)
        }

        row.levelState = TableRowLevelState.Closed

        attachedRowCount -= children.size

        // When there are many children, we may have to put back non-children rows
        // at close. Otherwise, there could be an empty area under the closed
        // row.

        if (attachedRowCount - (renderIndex - firstAttachedRowIndex) < addBelowCount) {
            attachedRowCount += addBelowCount
        }

        attachedRowCount = min(attachedRowCount, renderData.size)

        redraw()
    }

    fun getChildren(from: List<TableRow<T>>, fromIndex: Int, level: Int): List<TableRow<T>> {
        var index = fromIndex + 1
        val children = mutableListOf<TableRow<T>>()

        while (index < from.size) {
            val next = from[index]

            if (next.level > level) {
                children += next
                index ++
            } else {
                break
            }
        }

        return children
    }

    // -------------------------------------------------------------------------
    //  Sorting
    // -------------------------------------------------------------------------

    fun sort(ascending: Boolean, comparator: (T, T) -> Int) {
        fullData = if (! configuration.multiLevel) {
            if (ascending) {
                fullData.sortedWith { a, b -> comparator(a.data, b.data) }
            } else {
                fullData.sortedWith { a, b -> - comparator(a.data, b.data) }
            }
        } else {
            multiSort(fullData, ascending, comparator)
        }
    }

    /**
     * Utility class for multi-level sorting. Contains the children of the
     * row, so they will move together when sorted.
     */
    class SortEntry<T>(
        val row: TableRow<T>,
        val children: List<TableRow<T>>
    )

    /**
     * Sort a multi-level list of rows. Calls itself recursively for lower levels.
     */
    fun multiSort(
        data: List<TableRow<T>>,
        ascending: Boolean,
        comparator: (T, T) -> Int
    ): List<TableRow<T>> {

        // Do not perform sorting when the data contains a single row.
        if (data.size == 1) return data

        // Collect top level rows into a list of SortEntry objects. All lower level rows
        // are stored in SortEntry.children. Thus, we can sort the top level and lower
        // level rows will move with their parents.

        // Call multiSort recursively on children, so they are also sorted.

        val top = mutableListOf<SortEntry<T>>()
        var index = 0

        while (index < data.size) {
            val row = data[index]

            if (row.levelState == TableRowLevelState.Single) {
                top += SortEntry(row, emptyList())
            } else {
                val children = getChildren(data, index, row.level)
                top += SortEntry(row, multiSort(children, ascending, comparator))
                index += children.size
            }

            index += 1
        }

        // Sort the top level rows and merge all rows together to get the result.

        val sortedTop = if (ascending) {
            top.sortedWith { a, b -> comparator(a.row.data, b.row.data) }
        } else {
            top.sortedWith { a, b -> - comparator(a.row.data, b.row.data) }
        }

        val result = mutableListOf<TableRow<T>>()

        sortedTop.forEach {
            result += it.row
            result += it.children
        }

        return result
    }

}