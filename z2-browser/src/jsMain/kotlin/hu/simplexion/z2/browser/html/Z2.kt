package hu.simplexion.z2.browser.html

import hu.simplexion.z2.browser.css.CssClass
import hu.simplexion.z2.browser.css.addCss
import hu.simplexion.z2.browser.css.removeCss
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.event.Z2EventListener
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.appendText
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLElement

open class Z2(
    val parent: Z2? = null,
    val htmlElement: HTMLElement = document.createElement("div") as HTMLElement,
    classes: Array<out CssClass>? = null,
    val builder: (Z2.() -> Unit)? = null,
) {
    val style
        get() = htmlElement.style

    var zIndex: Int
        get() = style.zIndex.toInt()
        set(value) {
            style.zIndex = value.toString()
        }

    /**
     * Set `htmlElement.tabIndex`
     *
     * ```text
     * -1    not focusable
     * 0     in order in document source
     * N     before m > N but after 0
     * ```
     */
    var tabIndex: Int
        get() = htmlElement.tabIndex
        set(value) {
            htmlElement.tabIndex = value
        }

    val children = mutableListOf<Z2>()

    val listeners = mutableListOf<Z2EventListener>()

    init {
        // better do this directly here for performance
        // as this is a new element, there shouldn't be any classes
        classes?.let { htmlElement.className = it.joinToString(" ") { c -> c.name } }
        builder?.let { this.it() }
        parent?.let {
            it.htmlElement.append(this.htmlElement)
            it.children += this
        }
    }

    open fun main(): Z2 {
        return this
    }

    fun append(child: Z2) {
        children += child
        htmlElement.appendChild(child.htmlElement)
    }

    fun remove(child: Z2) {
        children -= child
        htmlElement.removeChild(child.htmlElement)
    }

    fun addClass(vararg classes: String): Z2 {
        htmlElement.addClass(*classes)
        return this
    }

    fun addCss(vararg classes: CssClass): Z2 {
        htmlElement.addCss(*classes)
        return this
    }

    fun addCss(classes: Iterable<CssClass>): Z2 {
        htmlElement.addCss(classes)
        return this
    }

    fun removeClass(vararg classes: String): Z2 {
        htmlElement.removeClass(*classes)
        return this
    }

    fun removeCss(vararg classes: CssClass): Z2 {
        htmlElement.removeCss(*classes)
        return this
    }

    fun replaceClass(vararg classes: String): Z2 {
        htmlElement.removeClass(*classes)
        if (classes.isNotEmpty()) htmlElement.addClass(classes.last())
        return this
    }

    fun replaceCss(vararg classes: CssClass): Z2 {
        htmlElement.removeCss(*classes)
        if (classes.isNotEmpty()) htmlElement.addCss(classes.last())
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
        EventCentral.detachAll(listeners)
        clear()
    }

    inline fun text(builder: () -> Any?) {
        builder()?.let { htmlElement.appendText(it.toString()) }
    }

    operator fun LocalizedText?.unaryPlus() {
        htmlElement.appendText(this.toString())
    }

    operator fun String?.unaryPlus() {
        this?.let { htmlElement.appendText(this) }
    }

    operator fun LocalizedIcon.unaryPlus() {
        icon(this@unaryPlus)
    }

    infix fun Z2.gridRow(value : String) {
        gridColumn = value
    }

    infix fun Z2.gridColumn(value : String) {
        gridColumn = value
    }

}