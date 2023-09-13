package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.fr
import hu.simplexion.z2.browser.material.menu.menuDivider
import hu.simplexion.z2.browser.material.menu.menuItem
import hu.simplexion.z2.browser.material.menu.more
import hu.simplexion.z2.browser.material.px


fun Z2.menuDemo() =

    surfaceContainerLow {

        grid {
            gridTemplateColumns = "max-content min-content"
            gridTemplateRows = 1.fr
            gridGap = 16.px

            text { "Menu with 1 item, normal icon" }
            more {
                menuItem(1, browserIcons.search, strings.menuItem1) { }
            }
        }

        grid {
            gridTemplateColumns = "max-content min-content"
            gridTemplateRows = 1.fr
            gridGap = 16.px

            text { "Menu with 2 items, inline icon" }
            more(inline = true) {
                menuItem(1, browserIcons.search, strings.menuItem1) { }
                menuDivider()
                menuItem(2, browserIcons.search, strings.menuItem2) { }
            }
        }

    }