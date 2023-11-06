package hu.simplexion.z2.browser.html

import kotlinx.browser.document
import org.w3c.dom.*

fun Z2.div(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("div") as HTMLDivElement, classes, builder)

fun Z2.grid(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("div") as HTMLDivElement, classes, builder)
        .apply { style.display = "grid" }

fun Z2.pre(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("pre") as HTMLPreElement, classes, builder)

fun Z2.input(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("input") as HTMLInputElement, classes, builder)

fun Z2.span(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("span") as HTMLSpanElement, classes, builder)

fun Z2.form(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("form") as HTMLFormElement, classes, builder)

fun Z2.a(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("a") as HTMLAnchorElement, classes, builder)

/**
 * A plain HTML table tag. Decided to use a different name because I use this
 * tag very rarely and mostly inside library components.
 */
fun Z2.tableHtml(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("table") as HTMLTableElement, classes, builder)

fun Z2.thead(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("thead") as HTMLTableSectionElement, classes, builder)

fun Z2.th(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("th") as HTMLTableCellElement, classes, builder)

fun Z2.tbody(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("tbody") as HTMLTableSectionElement, classes, builder)

fun Z2.tr(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("tr") as HTMLTableRowElement, classes, builder)

fun Z2.td(vararg classes: String, builder: Z2.() -> Unit = {  }): Z2 =
    Z2(this, document.createElement("td") as HTMLTableCellElement, classes, builder)

fun Z2.img(vararg classes: String, builder: Image.() -> Unit = {  }): Z2 =
    Image(this, classes, builder)
