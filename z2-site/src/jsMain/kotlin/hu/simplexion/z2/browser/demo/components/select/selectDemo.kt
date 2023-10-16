package hu.simplexion.z2.browser.demo.components.select

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.displayGrid
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.pre
import hu.simplexion.z2.browser.immaterial.select.SelectBase
import hu.simplexion.z2.browser.immaterial.select.SelectConfig
import hu.simplexion.z2.browser.immaterial.select.select
import hu.simplexion.z2.browser.immaterial.select.singleChipSelect
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import kotlinx.coroutines.delay

fun Z2.selectDemo() =
    surfaceContainerLow(displayGrid, gridGap24) {
        div(w400) {
            val items = (1..50).map { "item $it" }
            val feedback = pre {

            }

            select(
                items,
                label = strings.label,
                style = FieldStyle.Filled,
                onChange = { feedback.apply { + "select 1 change: ${it.value}\n" }}
            )

            select(
                items,
                label = strings.label,
                style = FieldStyle.Transparent,
                onChange = { feedback.apply { + "select 2 change: ${it.value}\n" }}
            )

            select(
                items,
                label = strings.label,
                style = FieldStyle.Outlined,
                onChange = { feedback.apply { + "select 3 change: ${it.value}\n" }}
            )

            select(
                items,
                label = strings.label,
                style = FieldStyle.Chip,
                onChange = { feedback.apply { + "select 4 change: ${it.value}\n" }}
            )

            singleChipSelect(
                items,
                label = strings.label,
                onChange = { feedback.apply { + "select 5 change: ${it.valueOrNull}\n" }}
            )

            SelectBase(
                this,
                FieldState().also {
                    it.label = "Label"
                },
                SelectConfig<String>().also {
                    it.trailingIcon = browserIcons.down
                },
                queryFun = {
                    delay(1500)
                    emptyList()
                }
            ).main()

            // move feedback to the end
            remove(feedback)
            append(feedback)
        }
    }
