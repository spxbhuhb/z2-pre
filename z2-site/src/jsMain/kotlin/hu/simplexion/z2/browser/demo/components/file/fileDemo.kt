package hu.simplexion.z2.browser.demo.components.file

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.components.file.fileSelect
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.high
import hu.simplexion.z2.browser.layout.low
import hu.simplexion.z2.browser.material.button.filledLaunchButton
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.snackbar.snackbar
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.i18n.locales.localized
import hu.simplexion.z2.commons.util.formatByteLength
import hu.simplexion.z2.commons.util.here
import hu.simplexion.z2.commons.util.hereAndNow
import kotlinx.datetime.Instant
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
            +strings.selectedFiles
            +"\n\n"
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
        addClass(widthFull)
        title(strings.file)

        val files = mutableListOf<File>()
        var disposition = ""
        var fileList: Z2? = null

        grid(gridGap16, overflowHidden, p16) {
            gridTemplateColumns = "1fr 1fr"
            gridTemplateRows = "min-content 400px"

            div(overflowYAuto) {
                gridRow = "1/3"
                div(labelMedium) { +strings.disposition }
                radioButtonGroup(dispositions.first(), dispositions) { disposition = it }
            }

            div {
                fileSelect { selected ->
                    val filtered = selected.filter { !files.any { f -> it.name == f.name } }
                    if (filtered.size != selected.size) snackbar(strings.fileAlreadyAdded)
                    files += selected
                    fileList?.fileList(files)
                }
            }

            grid(gridGap16) {
                fileList = this
                gridTemplateRows = "min-content 1fr"
                fileList(files)
            }
        }

        buttons {
            textButton(commonStrings.cancel) { closeWith(emptyList()) }
            textButton(commonStrings.add) { }
        }
    }

fun Z2.fileList(files: List<File>) {
    clear()

    val total = files.sumOf { it.size.toLong() }
    val totals = if (total == 0L) "" else total.formatByteLength(2, strings.zeroBytes, strings.bytes)
    div {
        + strings.filesToUpload
        span(pl16, bodySmall, onSurfaceVariantText) { + totals }
    }

    high(overflowYAuto) {
        for (file in files) {
            fileListEntry(file)
        }
    }
}

fun Z2.fileListEntry(file: File) {
    grid(gridGap8, mb16) {
        gridTemplateColumns = "1fr 5em min-content"

        div {
            div(mb4) { +file.name }
            div(bodySmall, onSurfaceVariantText) {
                // TODO remove the lastmodified magic
                val lastModified = (js("file.lastModified.toString()") as String).toLongOrNull()
                lastModified?.let { +Instant.fromEpochMilliseconds(lastModified.toLong()).here().localized }
            }
        }

        div(justifySelfEnd, alignSelfCenter, bodySmall, onSurfaceVariantText) {
            +file.size.formatByteLength(2, strings.zeroBytes, strings.bytes)
        }

        div(alignSelfCenter, pl8) {
            icon(browserIcons.cancel, cssClass = errorText).onClick { }
        }
    }
}