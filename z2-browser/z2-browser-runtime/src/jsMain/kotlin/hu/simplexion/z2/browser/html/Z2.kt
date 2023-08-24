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

    /**
     * Clear the content of this element. Call [dispose] of all children
     * and then remove all children. Clear the content of [htmlElement].
     *
     * Keeps itself functional, new children may be added and event listeners
     * are still in effect.
     *
     * [clear] is intentionally not open, cleanup functions should go
     * into [dispose].
     */
    fun clear() {
        for (child in children) {
            child.dispose()
        }
        children.clear()
        htmlElement.innerText = ""
    }

    /**
     * Clear the content of this element by calling [clear], detach
     * all even listeners. After [dispose] the element is considered
     * non-existing.
     */
    open fun dispose() {
        clear()
    }

    inline fun text(builder: () -> Any?) {
        builder()?.let { htmlElement.appendText(it.toString()) }
    }
}