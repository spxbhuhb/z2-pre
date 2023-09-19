package hu.simplexion.z2.browser.demo.components.file

import hu.simplexion.z2.browser.components.file.FileBundleInput
import hu.simplexion.z2.browser.components.file.FileBundleInputConfiguration
import hu.simplexion.z2.browser.components.file.fileSelect
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.button.filledLaunchButton
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.browser.material.vh
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.i18n.locales.localized
import hu.simplexion.z2.commons.util.hereAndNow
import org.w3c.files.File

val folders = listOf(
    "folder-1",
    "folder-2",
    "folder-3",
    "folder-4",
    "folder-5",
    "folder-6",
    "folder-7",
)


val types = listOf(
    "disposition-1",
    "disposition-2",
    "disposition-3",
    "disposition-4",
    "disposition-5",
    "disposition-6",
    "disposition-7",
)

fun Z2.fileDemo() =
    surfaceContainerLow(displayGrid, gridGap24) {
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

        val builder = FileBundleInput(this, FileBundleInputConfiguration(folders, types)).main()

        grid(pr16, pb16, pt16, gridAutoFlowColumn, gridAutoColumnsMinContent, justifyContentFlexEnd) {
            textButton(commonStrings.cancel) { closeWith(emptyList()) }
            textButton(commonStrings.add) { closeWith(builder.attachments + builder.mainFile) }
        }
    }
