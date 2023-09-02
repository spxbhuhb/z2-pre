package hu.simplexion.z2.browser.demo.search

import hu.simplexion.z2.browser.components.search.Search
import hu.simplexion.z2.browser.css.positionRelative
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.low

fun Z2.searchDemo() =

    low {
        grid(positionRelative) {
            gridTemplateColumns = "minmax(0,400px)"
            gridAutoRows = "min-content"
            gridGap = "16px"

            div { text { "Items: 'aa', 'ba', 'ca'" } }

            Search(this, { value -> listOf("aa", "ba", "ca").filter { value in it } }, strings.search) {  }

            Search(this, { value -> listOf("aa", "ba", "ca").filter { value in it } }, strings.search) {  }

        }
    }