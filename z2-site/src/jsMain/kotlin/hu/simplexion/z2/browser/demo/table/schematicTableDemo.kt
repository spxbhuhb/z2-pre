package hu.simplexion.z2.browser.demo.table


import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.schematicColumn
import hu.simplexion.z2.schematic.Schematic

private class Data : Schematic<Data>() {
    var v1 by string()
    var v2 by int()
}

fun Z2.schematicTableDemo() =
    tablePage<Data> {

        val data = (1..50).map {
            Data().apply {
                v1 = "s-$it"
                v2 = it
            }
        }

        title {
            text = strings.tableTitle
            searchable = true
            action(strings.addAccount) { }
        }

        search = true
        add = true
        export = true

        rowId = { it.v1 }
        query = { data }

        with(Data()) {
            schematicColumn { v1 }
            schematicColumn { v2 }
        }
    }