package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.css.pb12
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.radiobutton.radioButton
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.snackbar.snackbar

fun Z2.radioButtonDemo() =

    surfaceContainerLow {
        grid {
            gridTemplateColumns = "min-content"
            gridAutoRows = "min-content"
            gridGap = "16px"

            grid {
                gridTemplateColumns = "200px 200px 200px"
                div {
                    div(pb12) { text { "Standalone" } }
                    radioButton(true, disabled = false) { snackbar("Clicked!") }
                    radioButton(false, disabled = false) { snackbar("Clicked!") }
                    radioButton(selected = true, disabled = true) { snackbar("This message shouldn't be shown ever.") }
                    radioButton(selected = false, disabled = true) { snackbar("This message shouldn't be shown ever.") }
                }

                div {
                    div(pb12) { text { "Group" } }
                    val entries = listOf(strings.item1, strings.item2, strings.item3)
                    radioButtonGroup(entries.first(), entries) { snackbar("New value: $it") }
                }

                div {
                    div(pb12) { text { "Group - outlined" } }
                    val entries = listOf(strings.item1, strings.item2, strings.item3)
                    radioButtonGroup(entries.first(), entries, label = strings.radioButton, style = FieldStyle.Outlined) { snackbar("New value: $it") }
                }
            }
        }
    }