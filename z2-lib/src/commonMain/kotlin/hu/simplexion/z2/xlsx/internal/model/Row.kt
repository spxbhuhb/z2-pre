/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.xlsx.internal.model

import hu.simplexion.z2.xlsx.internal.dom.Node

internal class Row(val rowNumber: Int) : Node("row") {

    init {
        this["r"] = rowNumber.toString()
    }

    fun addCell(cell: Cell) = +cell

}
