package hu.simplexion.z2.browser.demo.pages

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLowest
import hu.simplexion.z2.browser.material.button.filledButton
import hu.simplexion.z2.browser.material.card.outlinedCard
import hu.simplexion.z2.browser.material.fr
import hu.simplexion.z2.browser.material.navigation.NavigationItem
import hu.simplexion.z2.browser.material.px

val adminItems = listOf(
    NavigationItem(null, strings.accounts, strings.accounts.support),
    NavigationItem(null, strings.roles, strings.accounts.support),
    NavigationItem(null, strings.languages, strings.accounts.support),
    NavigationItem(null, strings.interfaces, strings.accounts.support),
    NavigationItem(null, strings.securityPolicy, strings.accounts.support)
)

fun Z2.adminDemo() =
    surfaceContainerLowest(borderOutline) {
        div(titleLarge, pb24) {
            text { strings.administration }
        }

        grid {
            gridTemplateColumns = "repeat(auto-fit, 300px)"
            gridAutoRows = "min-content"
            gridGap = 8.px

            for (item in adminItems) {
                adminCard(item)
            }
        }
    }

fun Z2.adminCard(item: NavigationItem) {
    outlinedCard(item.label) {
        style.display = "grid"
        gridTemplateColumns = 1.fr
        gridTemplateRows = "min-content 1fr min-content"

        div(bodyMedium) {
            text { item.supportText }
        }

        div(justifySelfEnd, pt24) {
            filledButton(browserStrings.open) { }
        }
    }
}