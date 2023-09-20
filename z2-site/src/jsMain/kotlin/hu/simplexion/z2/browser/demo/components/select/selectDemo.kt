package hu.simplexion.z2.browser.demo.components.select

import hu.simplexion.z2.browser.css.displayGrid
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.pre
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.nonmaterial.select.select

fun Z2.selectDemo() =
    surfaceContainerLow(displayGrid, gridGap24) {
        div(w400) {
            val feedback = pre {

            }

            select(
                listOf(strings.item1, strings.item2, strings.item3),
                label = strings.label,
                style = FieldStyle.Filled,
                onChange = { feedback.apply { + "select 1 change: ${it.value}\n" }}
            )

            select(
                listOf(strings.item1, strings.item2, strings.item3),
                label = strings.label,
                style = FieldStyle.Transparent,
                onChange = { feedback.apply { + "select 2 change: ${it.value}\n" }}
            )

            select(
                listOf(strings.item1, strings.item2, strings.item3),
                label = strings.label,
                style = FieldStyle.Outlined,
                onChange = { feedback.apply { + "select 3 change: ${it.value}\n" }}
            )

            // move feedback to the end
            remove(feedback)
            append(feedback)
        }
    }
