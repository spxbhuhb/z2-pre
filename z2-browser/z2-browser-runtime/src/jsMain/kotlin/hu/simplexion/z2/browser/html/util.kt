package hu.simplexion.z2.browser.html

import org.w3c.dom.HTMLElement
import org.w3c.dom.NodeList
import org.w3c.dom.get

typealias Z2Builder = Z2.() -> Unit

fun NodeList.forEach(func : HTMLElement.() -> Unit) {
    val size = this.length
    for (i in 0 until size) {
        (this[i] as? HTMLElement)?.func()
    }
}