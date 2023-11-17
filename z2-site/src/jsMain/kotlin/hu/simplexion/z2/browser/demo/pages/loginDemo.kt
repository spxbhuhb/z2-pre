package hu.simplexion.z2.browser.demo.pages

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.button.filledButton
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.fr
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.textfield.outlinedTextField
import org.w3c.dom.HTMLInputElement

fun Z2.loginDemo() =
    surfaceContainerLow(wFull, heightFull, p0, m0, displayFlex, alignItemsCenter, justifyContentCenter) {
        grid(gridGap8) {
            gridTemplateColumns = "min-content"
            gridAutoRows = "min-content"

            div(p24, borderRadius8) {
                style.border = "1px solid lightgray"

                grid(gridGap32) {
                    gridTemplateColumns = 400.px
                    gridAutoRows = "min-content"

                    div(titleSmall, justifySelfCenter) {
                        text { strings.loginSupport }
                    }

                    div(titleLarge, justifySelfCenter) {
                        text { strings.login }
                    }

                    grid(gridGap24) {
                        gridTemplateColumns = 1.fr
                        gridTemplateRows = "min-content min-content"

                        outlinedTextField("", strings.account)
                        outlinedTextField("", strings.password)
                            .apply { (input.htmlElement as HTMLInputElement).type = "password" }
                    }

                    grid(gridGap24, positionRelative) {
                        gridTemplateColumns = "min-content 1fr min-content"
                        gridTemplateRows = "min-content"

                        style.left = "-8px"

                        textButton(strings.forgottenPassword) { }
                        textButton(strings.registration) { }
                        filledButton(strings.login) { }
                    }
                }
            }

            grid {
                gridTemplateColumns = "1fr repeat(3, min-content)"
                gridTemplateRows = "min-content"
                gridGap = 16.px

                textButton(strings.english) { }
                textButton(strings.help) { }
                textButton(strings.privacy) { }
                textButton(strings.term) { }
            }
        }
    }