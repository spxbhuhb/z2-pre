package hu.simplexion.z2.browser.html

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.NodeList
import org.w3c.dom.get

typealias Z2Builder = Z2.() -> Unit

fun NodeList.forEach(func : (HTMLElement) -> Unit) {
    val size = this.length
    for (i in 0 until size) {
        (this[i] as? HTMLElement)?.let { func(it) }
    }
}

class NodeListIterator(
    val nodeList : NodeList
) : Iterator<HTMLElement?> {

    var current = 0

    override fun hasNext(): Boolean {
        return (current < nodeList.length)
    }

    override fun next(): HTMLElement? {
        current++
        return nodeList[current] as? HTMLElement
    }
}

operator fun NodeList.iterator() : Iterator<HTMLElement?> =
    NodeListIterator(this)

/**
 * Focus the next element after the current one.
 * TODO sort elements by tab index
 */
fun HTMLElement.focusNext() : Boolean {
    var chooseNext = false


    for (element in document.querySelectorAll("input,[tabindex]")) {
        when {
            element == null -> continue
            element == this -> {
                chooseNext = true
                continue
            }
            chooseNext -> {
                element.focus()
                return true
            }
        }
    }

    return false
}