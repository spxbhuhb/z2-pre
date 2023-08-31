package hu.simplexion.z2.browser.demo.search

import hu.simplexion.z2.browser.components.search.Search
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.button.*
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.snackbar.snackbar

fun Z2.searchDemo() =

    low {
        grid {
            gridTemplateColumns = "min-content"
            gridAutoRows = "min-content"
            gridGap = "16px"

            div { text { "Items: 'aa', 'ba', 'ca'" } }

            Search(this, { value -> listOf("aa", "ba", "ca").filter { value in it } }) {  }
        }
    }