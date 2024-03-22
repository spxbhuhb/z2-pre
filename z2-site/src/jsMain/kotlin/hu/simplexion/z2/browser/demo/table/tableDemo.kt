package hu.simplexion.z2.browser.demo.table


import hu.simplexion.z2.browser.css.backgroundTransparent
import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.builders.TableBuilder
import hu.simplexion.z2.browser.immaterial.table.table
import hu.simplexion.z2.browser.layout.container
import hu.simplexion.z2.browser.layout.surfaceContainerLowest
import hu.simplexion.z2.browser.material.em

fun <T> Z2.tablePage(builder: TableBuilder<T>.() -> Unit) =
    surfaceContainerLowest(borderOutline) {
        container(p0, backgroundTransparent, scroll = false) {
            table(builder)
        }
    }

class Row(
    val v1: String,
    val v2: Int
)

fun Z2.tableDemo() =
    tablePage<Row> {

        val data = (1..50).map {
            Row("s-$it", it)
        }

        title {
            text = strings.tableTitle
            action(strings.addAccount) { }
        }

        search = true
        add = true
        export = true

        rowId = { it.v1 }
        query = { data }

        column {
            label = strings.headerA
            render = { text { it.v1 } }
            comparator = { a, b -> a.v2.compareTo(b.v2) }
            initialSize = 10.em
        }

        column {
            label = strings.headerB
            render = { text { it.v2 } }
            comparator = { a, b -> a.v2.compareTo(b.v2) }
        }

    }