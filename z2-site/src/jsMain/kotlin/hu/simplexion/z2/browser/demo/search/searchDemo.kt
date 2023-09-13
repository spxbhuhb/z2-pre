package hu.simplexion.z2.browser.demo.search

import hu.simplexion.z2.browser.components.search.query
import hu.simplexion.z2.browser.components.search.search
import hu.simplexion.z2.browser.css.positionRelative
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLow

fun Z2.searchDemo() =

    surfaceContainerLow {
        grid(positionRelative) {
            gridTemplateColumns = "minmax(0,400px)"
            gridAutoRows = "min-content"
            gridGap = "16px"

            div { text { "Items: 'aa', 'ba', 'ca'" } }

            val list = listOf("aa", "ba", "ca")

            search<String>(strings.search) query { v -> list.filter { v in it } }
        }
    }