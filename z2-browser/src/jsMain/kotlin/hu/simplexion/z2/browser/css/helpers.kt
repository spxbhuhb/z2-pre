package hu.simplexion.z2.browser.css

import hu.simplexion.z2.browser.html.Z2
import kotlinx.dom.hasClass
import org.w3c.dom.Element

infix fun Z2.css(className: String): Z2 {
    addClass(className)
    return this
}

infix fun Z2.css(cssClass: CssClass): Z2 {
    addClass(cssClass.name)
    return this
}

val String.css
    get() = CssClass(this)

fun Element.addCss(vararg cssClasses: CssClass): Boolean {
    val missingClasses = cssClasses.filterNot { hasClass(it.name) }
    if (missingClasses.isNotEmpty()) {
        val presentClasses = className.trim()
        className = buildString {
            append(presentClasses)
            if (presentClasses.isNotEmpty()) {
                append(" ")
            }
            missingClasses.joinTo(this, " ") { it.name }
        }
        return true
    }
    return false
}

fun Element.addCss(cssClasses: Iterable<CssClass>): Boolean {
    val missingClasses = cssClasses.filterNot { hasClass(it.name) }
    if (missingClasses.isNotEmpty()) {
        val presentClasses = className.trim()
        className = buildString {
            append(presentClasses)
            if (presentClasses.isNotEmpty()) {
                append(" ")
            }
            missingClasses.joinTo(this, " ") { it.name }
        }
        return true
    }
    return false
}

fun Element.removeCss(vararg cssClasses: CssClass): Boolean {
    if (cssClasses.any { hasClass(it.name) }) {
        val toBeRemoved = cssClasses.map { it.name }.toSet()
        className = className.trim().split("\\s+".toRegex()).filter { it !in toBeRemoved }.joinToString(" ")
        return true
    }
    return false
}