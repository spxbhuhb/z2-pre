package hu.simplexion.z2.browser.components.file

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.components.select.select
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.scrolledBoxWithLabel
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.snackbar.snackbar
import hu.simplexion.z2.browser.material.stateLayer
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.locales.localized
import hu.simplexion.z2.commons.util.formatByteLength
import hu.simplexion.z2.commons.util.here
import kotlinx.datetime.Instant
import org.w3c.files.File

class FileBundleInput<FT,DT>(
    parent: Z2,
    val config: FileBundleInputConfiguration<FT,DT>
) : Z2(parent) {

    var folder : FT = config.folders.first()
    var type: DT? = null

    var mainFile = emptyList<File>()
    val attachments = mutableListOf<File>()

    lateinit var mainList: Z2
    lateinit var attachmentList: Z2
    lateinit var summary: Z2
    lateinit var mainSelect: FileSelect
    lateinit var attachmentSelect: FileSelect

    override fun main(): FileBundleInput<FT,DT> {
        addClass(overflowHidden, displayGrid, pl24, pr24, positionRelative, boxSizingBorderBox, gridGap16)
        gridTemplateColumns = "max-content minmax(600px,1fr)"
        gridTemplateRows = "min content minmax(200px, 1fr)"

        div(displayGrid) {
            gridTemplateColumns = "1fr max-content"
            gridColumn = "1/3"
            select(config.folders, folder, label = config.folderLabel, style = FieldStyle.Outlined).also {
                it.config.supportEnabled = false
            }
            div(justifySelfEnd, alignSelfEnd, pl24) { summary = this }
        }

        scrolledBoxWithLabel(config.typeLabel) {
            addClass(pr16)
            radioButtonGroup(type, config.types, itemBuilderFun = config.typeRenderFun) { type = it }
        }

        grid(positionRelative, gridGap16, overflowHidden) {
            gridTemplateColumns = "1fr"
            gridTemplateRows = "min-content 1fr"

            fileSelect(fileBrowserOnClick = false, fileSelectedFun = ::onMainSelect, multiple = false) {
                mainSelect = this
                scrolledBoxWithLabel(this@FileBundleInput.config.file) {
                    grid(gridGap16, pl8, pr8) {
                        mainList = this
                    }
                }
            }

            fileSelect(fileBrowserOnClick = false, fileSelectedFun = ::onAttachmentSelect) {
                config.dropEnabled = false
                attachmentSelect = this
                addClass(positionRelative, heightFull, overflowHidden)
                scrolledBoxWithLabel(this@FileBundleInput.config.attachments) {
                    grid(gridGap16, pl8, pr8) {
                        attachmentList = this
                    }
                }
            }
        }

        refresh()

        return this
    }

    fun refresh() {
        summary.summary()
        mainList.fileList(config.dropFileHereLabel, active = true, false, mainFile, mainSelect) {
            mainFile = emptyList()
        }

        val attachmentLabel = if (mainFile.isNotEmpty()) {
            config.dropAttachmentHereLabel
        } else {
            config.selectMainFirst
        }

        attachmentList.fileList(attachmentLabel, active = mainFile.isNotEmpty(), true, attachments, attachmentSelect) { file ->
            attachments.removeAll { it.name == file.name }
        }

        attachmentSelect.config.dropEnabled = mainFile.isNotEmpty()
    }

    fun onMainSelect(selected: List<File>) {
        mainFile = selected
        refresh()
    }

    fun onAttachmentSelect(selected: List<File>) {
        val filtered = selected.filter { ! attachments.any { f -> it.name == f.name } && it.name != mainFile.firstOrNull()?.name }
        if (filtered.size != selected.size) snackbar(config.fileAlreadyAddedLabel)
        attachments += filtered
        refresh()
    }

    fun Z2.summary() {
        clear()

        val total = mainFile.sumOf { it.size.toLong() } + attachments.sumOf { it.size.toLong() }

        grid(bodySmall, onSurfaceVariantText, pl16) {
            gridTemplateColumns = "1fr max-content"
            div {
                if (mainFile.isNotEmpty() || attachments.isNotEmpty()) {
                    + "${attachments.size + mainFile.size} "
                    + config.filesSelectedLabel
                } else {
                    + config.noFileSelected
                }
            }
            div(justifySelfEnd) {
                if (total != 0L) {
                    addClass(pl16)
                    + total.formatByteLength(2, browserStrings.zeroBytes, browserStrings.bytes)
                }
            }
        }
    }

    fun Z2.fileList(
        label: LocalizedText,
        active : Boolean,
        multiple: Boolean,
        files: List<File>,
        select: FileSelect,
        remove: (file: File) -> Unit
    ) {
        clear()

        if (files.isNotEmpty()) {
            grid(overflowYAuto, gridGap8, pb4) {
                for (file in files) {
                    fileListEntry(file, select, remove)
                }
            }
        }

        if (multiple || files.isEmpty()) {
            if (active) {
                textButton(label, false) { select.openFileBrowser() } css justifySelfCenter
            } else {
                div(justifySelfCenter, onSurfaceVariantText, bodyMedium, pt20) { + label }
            }
        }
    }

    fun Z2.fileListEntry(file: File, select: FileSelect, remove: (file: File) -> Unit) {
        grid(gridGap8, positionRelative, pt8, pb8, pl12, pr8) {
            stateLayer().addClass(borderRadius4)

            gridTemplateColumns = "1fr 5em 32px"

            div {
                div(mb4) { + file.name }
                div(bodySmall, onSurfaceVariantText) {
                    // TODO remove the lastmodified magic
                    val lastModified = (js("file.lastModified.toString()") as String).toLongOrNull()
                    lastModified?.let { + Instant.fromEpochMilliseconds(lastModified.toLong()).here().localized }
                }
            }

            div(justifySelfEnd, alignSelfCenter, bodySmall, onSurfaceVariantText) {
                + file.size.formatByteLength(2, browserStrings.zeroBytes, browserStrings.bytes)
            }

            div(alignSelfCenter, pl8) {
                icon(browserIcons.cancel, cssClass = errorText).onClick {
                    it.stopPropagation()
                    remove(file)
                    select.inputElement.value = ""
                    refresh()
                }
            }
        }
    }
}