package hu.simplexion.z2.browser.demo.pages

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.button.filledButton
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.fr
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.textfield.outlinedTextField
import hu.simplexion.z2.commons.i18n.LocalizedTextStore
import hu.simplexion.z2.commons.util.UUID
import org.w3c.dom.HTMLInputElement

object loginStrings : LocalizedTextStore(UUID("72c9ec55-0e66-4181-96f9-d9009b03712e")) {
    val loginSupport by "Z2 Browser Components Demo"
    val login by "Sign in"
    val account by "Account"
    val password by "Password"
    val forgottenPassword by "Forgot password?"
    val help by "Help"
    val privacy by "Privacy"
    val term by "Terms"
    val english by "English"
    val registration by "Create account"
}

fun Z2.loginDemo() =
    low(wFull, hFull, p0, m0, displayFlex, alignItemsCenter, justifyContentCenter) {
        grid(gridGap8) {
            gridTemplateColumns = "min-content"
            gridAutoRows = "min-content"

            div(p24, borderRadius8) {
                style.border = "1px solid lightgray"

                grid(gridGap32) {
                    gridTemplateColumns = 400.px
                    gridAutoRows = "min-content"

                    div(titleSmall, justifySelfCenter) {
                        text { loginStrings.loginSupport }
                    }

                    div(titleLarge, justifySelfCenter) {
                        text { loginStrings.login }
                    }

                    grid(gridGap24) {
                        gridTemplateColumns = 1.fr
                        gridTemplateRows = "min-content min-content"

                        outlinedTextField("", loginStrings.account)
                        outlinedTextField("", loginStrings.password)
                            .apply { (input.htmlElement as HTMLInputElement).type = "password" }
                    }

                    grid(gridGap24, positionRelative) {
                        gridTemplateColumns = "min-content 1fr min-content"
                        gridTemplateRows = "min-content"

                        style.left = "-8px"

                        textButton(loginStrings.forgottenPassword) { }
                        textButton(loginStrings.registration) { }
                        filledButton(loginStrings.login) { }
                    }
                }
            }

            grid {
                gridTemplateColumns = "1fr repeat(3, min-content)"
                gridTemplateRows = "min-content"
                gridGap = 16.px

                textButton(loginStrings.english) { }
                textButton(loginStrings.help) { }
                textButton(loginStrings.privacy) { }
                textButton(loginStrings.term) { }
            }
        }
    }