/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.table

import hu.simplexion.z2.browser.html.Z2

/**
 * State of one table row.
 *
 * @param     index        The index of the row in [ZkTable.fullData].
 * @param     data         Data of the row.
 * @property  element      The `tr` element added to the table.
 * @property  height       Height of the row in pixels, calculated after it is added to the DOM.
 * @property  level        Level of the row for multi-level tables.
 * @property  levelState   State of the row for multi-level tables.
 */
class TableRow<T>(
    var index : Int,
    var data: T,
) {
    var element: Z2? = null
    var height: Double? = null
    var level = 0
    var levelState: TableRowLevelState = TableRowLevelState.Single
}