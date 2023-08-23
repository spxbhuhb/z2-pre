package hu.simplexion.z2.browser.html

import kotlinx.dom.addClass
import kotlinx.dom.appendText
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLElement

open class Z2(
    val parent : Z2? = null,
    val htmlElement: HTMLElement,
    classes : Array<out String>,
    val builder: (Z2.() -> Unit)? = null,
) {
    val style
        get() = htmlElement.style

    val children = mutableListOf<Z2>()

    init {
        builder?.let { this.it() }
        htmlElement.addClass(*classes)
        parent?.htmlElement?.append(this.htmlElement)
    }

    fun append(child: Z2) {
        children += child
        htmlElement.appendChild(child.htmlElement)
    }

    fun remove(child: Z2) {
        children -= child
        htmlElement.removeChild(child.htmlElement)
    }

    fun addClass(vararg classes : String) : Z2 {
        htmlElement.addClass(*classes)
        return this
    }

    fun removeClass(vararg classes : String) : Z2 {
        htmlElement.removeClass(*classes)
        return this
    }

    fun focus() {
        htmlElement.focus()
    }

    fun clear() {
        for (child in children) {
            child.clear()
        }
        children.clear()
        htmlElement.innerText = ""
    }

    inline fun text(builder: () -> Any?) {
        builder()?.let { htmlElement.appendText(it.toString()) }
    }
}