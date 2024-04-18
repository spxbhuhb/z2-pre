package hu.simplexion.z2.browser.material.popup

import hu.simplexion.z2.browser.css.css
import hu.simplexion.z2.browser.html.Z2
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

object Popups : Z2(
    null,
    document.createElement("div") as HTMLDivElement,
    arrayOf("global-popup-container".css),
    { document.body?.appendChild(this.htmlElement) }
)