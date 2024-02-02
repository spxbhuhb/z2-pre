package hu.simplexion.z2.browser.material.searchbar

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.css
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.localization.text.LocalizedText
import org.w3c.dom.HTMLInputElement

fun Z2.searchBar(placeholder : LocalizedText = browserStrings.searchHint) =
    div("search-bar-container".css) {
        val container = this
        icon(browserIcons.search, cssClass = "search-bar-leading-icon".css)
        input("search-bar-input".css, "body-large".css) {
            (htmlElement as HTMLInputElement).placeholder = placeholder.toString()
            onFocus { container.addClass("search-bar-active")}
            onBlur { container.removeClass("search-bar-active")}
        }
        icon(browserIcons.filter, cssClass = "search-bar-trailing-icon".css)
    }

interface SearchProvider {
    fun onTextChange(value : String) {  }
    fun onEnter(value : String) {  }
    fun onEscape(value : String) {  }
}