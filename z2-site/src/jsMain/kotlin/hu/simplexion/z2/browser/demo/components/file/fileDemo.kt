package hu.simplexion.z2.browser.demo.components.file

import hu.simplexion.z2.browser.components.file.FileBundleBuilder
import hu.simplexion.z2.browser.components.file.FileBundleBuilderConfiguration
import hu.simplexion.z2.browser.components.file.fileSelect
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.button.filledLaunchButton
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.browser.material.vh
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.i18n.locales.localized
import hu.simplexion.z2.commons.util.hereAndNow
import org.w3c.files.File

val dispositions = listOf(
    "disposition-1",
    "disposition-2",
    "disposition-3",
    "disposition-4",
    "disposition-5",
    "disposition-6",
    "disposition-7",
)

fun Z2.fileDemo() =
    low(displayGrid, gridGap24) {
        gridTemplateColumns = "1fr 1fr"
        gridTemplateRows = "1fr"

        val feedback = pre(overflowXAuto) {
            + strings.selectedFiles
            + "\n\n"
        }

        fun feedback(files: List<File>) {
            for (file in files) {
                feedback.htmlElement.innerText += hereAndNow().localized + "  " + file.name + "  " + file.size + "\n"
            }
        }

        grid(gridGap16, gridAutoFlowRow) {
            filledLaunchButton(strings.fileSelectDialog) {
                fileSelectDialog().show().also { feedback(it) }
            }

            fileSelect { feedback(it) }
        }
    }

fun fileSelectDialog() =
    modal<List<File>> {
        style.maxHeight = 90.vh
        addClass(positionRelative, displayGrid)
        gridTemplateColumns = "1fr"
        gridTemplateRows = "min-content 1fr min-content"

        title(strings.file)

        val builder = FileBundleBuilder(this, FileBundleBuilderConfiguration(dispositions))

        grid(pr16, pb16, gridAutoFlowColumn, gridAutoColumnsMinContent, justifyContentFlexEnd) {
            textButton(commonStrings.cancel) { closeWith(emptyList()) }
            textButton(commonStrings.add) { closeWith(builder.files) }
        }
    }
