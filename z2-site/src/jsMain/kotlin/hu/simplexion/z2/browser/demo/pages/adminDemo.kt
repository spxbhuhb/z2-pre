package hu.simplexion.z2.browser.demo.pages

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLowest
import hu.simplexion.z2.browser.material.button.filledButton
import hu.simplexion.z2.browser.material.card.outlinedCard
import hu.simplexion.z2.browser.material.fr
import hu.simplexion.z2.browser.material.navigation.NavigationItem
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.commons.i18n.LocalizedTextStore

object adminStrings : LocalizedTextStore() {
    val administration by "Administration"

    val accounts by "Accounts"
    val accountsSupport by "Create, edit, lock and unlock accounts, assign roles, give new password to users."

    val interfaces by "Interfaces"
    val interfacesSupport by "Manage connections to other systems, such as e-mail or database servers."

    val languages by "Languages"
    val languagesSupport by "Register new languages. Translate user interface labels and icons."

    val roles by "Roles"
    val rolesSupport by "List top-level user roles, create new ones, list users by role."

    val securityPolicy by "Security Policy"
    val securityPolicySupport by "Set the security policy. Allowed login attempts, password strength etc."
}

val adminItems = listOf(
    NavigationItem(null, adminStrings.accounts, adminStrings.accountsSupport),
    NavigationItem(null, adminStrings.roles, adminStrings.rolesSupport),
    NavigationItem(null, adminStrings.languages, adminStrings.languagesSupport),
    NavigationItem(null, adminStrings.interfaces, adminStrings.interfacesSupport),
    NavigationItem(null, adminStrings.securityPolicy, adminStrings.securityPolicySupport)
)

fun Z2.adminDemo() =
    surfaceContainerLowest(borderOutline) {
        div(titleLarge, pb24) {
            text { adminStrings.administration }
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