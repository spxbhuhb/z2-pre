package hu.simplexion.z2.browser.demo.pages.administration

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.demo.icons
import hu.simplexion.z2.browser.demo.pages.administration.locale.LanguagesList
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLowest
import hu.simplexion.z2.browser.material.button.filledButton
import hu.simplexion.z2.browser.material.card.outlinedCard
import hu.simplexion.z2.browser.material.fr
import hu.simplexion.z2.browser.material.navigation.NavigationItem
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object administrationRouter : NavRouter() {
    override val label = strings.administration
    override val icon = icons.administration

    override var default : Z2Builder = { administration() }

    // @formatter:off
    val impressum        by render(strings.impressum, icons.impressum)           {  }
    val template         by render(strings.template, icons.template)             {  }
    val languages        by LanguagesList

    val account          by render(strings.account, icons.accounts)             {  }
    val roles            by render(strings.role, icons.roles)                   {  }
    val securityPolicy   by render(strings.securityPolicy, icons.securityPolicy) {  }

    val connections      by render(strings.connection, icons.interfaces)         {  }
    val history          by render(strings.history, icons.history)             {  }
    // @formatter:on
}

fun Z2.administration() =
    surfaceContainerLowest(borderOutline) {
        div(titleLarge, pb24) {
            text { strings.administration }
        }

        grid {
            gridTemplateColumns = "repeat(auto-fit, 300px)"
            gridAutoRows = "min-content"
            gridGap = 8.px

            for (item in administrationRouter.targets) {
                adminCard(NavigationItem(item.icon, item.label) { item.open() })
            }
        }
    }

fun Z2.adminCard(item: NavigationItem) {
    outlinedCard(item.label) {
        style.display = "grid"
        gridTemplateColumns = 1.fr
        gridTemplateRows = "min-content 1fr min-content"

        div(bodyMedium) {
            text { item.label?.support }
        }

        div(justifySelfEnd, pt24) {
            filledButton(browserStrings.open) { }
        }
    }
}

fun Z2.accounts() =
    div {
        text { "accounts " }
    }