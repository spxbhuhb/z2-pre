package hu.simplexion.z2.browser.demo.components.file

import hu.simplexion.z2.browser.components.file.bundle.FileBundleInput
import hu.simplexion.z2.browser.components.file.bundle.FileBundleInputConfiguration
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

fun types(index : Int) : List<String> =
    (0..index).map { "document type $index.$it" }

val folders = listOf(
    "folder-1",
    "folder-2",
    "folder-3",
    "folder-4",
    "folder-5",
    "folder-6",
    "folder-7",
)

val types = folders.mapIndexed { i, f -> f to types(i) }.toMap()

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

        val config = FileBundleInputConfiguration(folders, types, listOf("docx", "heic"), 1_000_000L)

        val bundle = FileBundleInput(this, config).main()

        grid(pr16, pb16, pt16, gridAutoFlowColumn, gridAutoColumnsMinContent, justifyContentFlexEnd) {
            textButton(commonStrings.cancel) { closeWith(emptyList()) }
            textButton(commonStrings.add) {
                if (!bundle.validate()) return@textButton
                closeWith(bundle.attachments + bundle.mainFile)
            }
        }
    }
