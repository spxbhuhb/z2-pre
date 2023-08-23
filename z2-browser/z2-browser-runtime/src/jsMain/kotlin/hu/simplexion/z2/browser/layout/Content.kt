package hu.simplexion.z2.browser.layout

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.Router
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

object Content : Z2(
    null,
    document.createElement("div") as HTMLDivElement,
    arrayOf("layout-content"),
    { document.body?.appendChild(this.htmlElement) }
) {
    var defaultLayout : Z2.(router: Router<Z2>, nav: Z2Builder, content: Z2Builder) -> Unit = { _,_,_ -> }
}