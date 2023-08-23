package hu.simplexion.z2.browser.html

import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

fun Z2.grid(vararg classes: String, builder: Z2.() -> Unit): Z2 =
    Z2(this, document.createElement("div") as HTMLDivElement, classes, builder)
        .apply { style.display = "grid" }

var Z2.gridTemplateRows: String
    get() = style.getPropertyValue("grid-template-rows")
    set(value) {
        style.setProperty("grid-template-rows", value)
    }

var Z2.gridTemplateColumns: String
    get() = style.getPropertyValue("grid-template-columns")
    set(value) {
        style.setProperty("grid-template-columns", value)
    }

var Z2.gridAutoColumns: String
    get() = style.getPropertyValue("grid-auto-columns")
    set(value) {
        style.setProperty("grid-auto-columns", value)
    }

var Z2.gridAutoRows: String
    get() = style.getPropertyValue("grid-auto-rows")
    set(value) {
        style.setProperty("grid-auto-rows", value)
    }

var Z2.gridGap: String
    get() = style.getPropertyValue("grid-gap")
    set(value) {
        style.setProperty("grid-gap", value)
    }

var Z2.rowGap: String
    get() = style.getPropertyValue("row-gap")
    set(value) {
        style.setProperty("row-gap", value)
    }

var Z2.columnGap: String
    get() = style.getPropertyValue("column-gap")
    set(value) {
        style.setProperty("column-gap", value)
    }

var Z2.gridColumn: String
    get() = style.getPropertyValue("grid-column")
    set(value) {
        style.setProperty("grid-column", value)
    }

var Z2.gridRow: String
    get() = style.getPropertyValue("grid-row")
    set(value) {
        style.setProperty("grid-row", value)
    }