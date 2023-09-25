/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.nonmaterial.table

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.nonmaterial.table.builders.TitleBuilder
import hu.simplexion.z2.commons.i18n.LocalizedIcon

/**
 * @property  add             When true a plus icon is added to the title bar. Click on the icon calls [onAddRow].
 * @property  search          When true a search input and icon is added to the title bar. Enter in the search field
 *                            or click on the icon calls [onSearch].
 * @property  oneClick        When true single clicks are treated as double clicks (call onDblClick).
 * @property  export          When true an export icon is added to the title bar. Calls [onExportCsv].
 * @property  exportFiltered  When true, the table exports only rows matching the current filter. Default is false.
 * @property  exportHeaders    When true, CSV export contains header row with header labels. Default is false.
 * @property  rowHeight       Height (in pixels) of one table row, used when calculating row positions for virtualization.
 */
open class TableConfiguration<T> {

    open var title = false
    open var add = false
    open var search = false
    open var export = false
    open var exportFiltered = false
    open var exportHeaders = false
    open var fixRowHeight = true
    open var fixHeaderHeight = true
    open var oneClick = false
    open var runQueryOnResume = true
    open var icon: LocalizedIcon? = null
    open var multiLevel = false
    open val rowHeight = 42

    open var titleBuilder: TitleBuilder<T>? = null

    open var searchBar: (Z2.() -> Unit)? = null

    open var doubleClickFun : ((T) -> Unit)? = null
}