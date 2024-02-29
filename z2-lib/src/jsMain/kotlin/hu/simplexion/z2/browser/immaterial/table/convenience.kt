package hu.simplexion.z2.browser.immaterial.table

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.builders.TableBuilder

fun <T> Z2.table(
    builder: TableBuilder<T>.() -> Unit
) = Table(this) { TableBuilder(it).apply { builder() } }


