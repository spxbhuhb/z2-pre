package hu.simplexion.z2.browser.nonmaterial.file.bundle

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.scrolledBoxWithLabel
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.radiobutton.RadioButtonGroup
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.snackbar.snackbar
import hu.simplexion.z2.browser.material.stateLayer
import hu.simplexion.z2.browser.nonmaterial.file.FileSelect
import hu.simplexion.z2.browser.nonmaterial.file.fileSelect
import hu.simplexion.z2.browser.nonmaterial.select.SelectBase
import hu.simplexion.z2.browser.nonmaterial.select.select
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.locales.localized
import hu.simplexion.z2.commons.util.formatByteLength
import hu.simplexion.z2.commons.util.here
import kotlinx.datetime.Instant
import org.w3c.files.File

open class FileBundleInput<FT, DT>(
    parent: Z2,
    val config: FileBundleInputConfiguration<FT, DT>
) : Z2(parent) {

    var touched: Boolean = false
    var folder: FT? = null
    var type: DT? = null

    var mainFile = emptyList<File>()
    val attachments = mutableListOf<File>()

    lateinit var typeBox: Z2
    lateinit var typeList: Z2
    lateinit var mainList: Z2
    lateinit var mainBox: Z2
    lateinit var attachmentList: Z2
    lateinit var attachmentBox: Z2
    lateinit var summary: Z2
    lateinit var folderSelect: SelectBase<FT>
    lateinit var typeSelect: RadioButtonGroup<DT>
    lateinit var mainSelect: FileSelect
    lateinit var attachmentSelect: FileSelect

    override fun main(): FileBundleInput<FT, DT> {
        addClass(overflowHidden, displayGrid, pl24, pr24, positionRelative, boxSizingBorderBox, gridGap16)
        gridTemplateColumns = if (config.showFolderAndTypeSelect) {
            "minmax(300px,max-content) minmax(600px,1fr)"
        } else {
            "minmax(600px,1fr)"
        }
        gridTemplateRows = "min-content minmax(300px, 1fr)"

        folderSelectAndSummary()
        typeSelect()

        grid(positionRelative, gridGap16, overflowHidden) {
            gridTemplateColumns = "1fr"
            gridTemplateRows = "min-content 1fr"

            mainFileSelect()
            attachmentFileSelect()
        }

        refresh()

        return this
    }

    fun Z2.folderSelectAndSummary() {
        div(displayGrid) {
            gridTemplateColumns = "1fr max-content"
            gridColumn = if (config.showFolderAndTypeSelect) "1/3" else "1/2"

            folderSelectBuild()
            summaryBuild()
        }
    }

    fun Z2.folderSelectBuild() {
        if (! config.showFolderAndTypeSelect) {
            div()
            return
        }

        select(config.folders, label = config.folderLabel, style = FieldStyle.Outlined) {
            folder = it.value
            type = null
            folderSelect.state.error = false
            refresh()
        }.also {
            folderSelect = it
            it.config.itemTextFun = config.folderTextFun
            it.config.supportEnabled = false
            folder?.apply { it.value = this } // FIXME convert select config into a select builder
        }
    }

    fun Z2.summaryBuild() {
        div(justifySelfEnd, alignSelfEnd, pl24) { summary = this }
    }

    fun Z2.typeSelect() {
        if (!config.showFolderAndTypeSelect) {
            return
        }

        scrolledBoxWithLabel(config.typeLabel) {
            addClass(pr16)
            typeList = this
        }.also {
            typeBox = it
        }
    }

    fun Z2.mainFileSelect() {
        fileSelect(fileBrowserOnClick = false, fileSelectedFun = ::onMainSelect, multiple = false) {
            mainSelect = this
            scrolledBoxWithLabel(this@FileBundleInput.config.file) {
                grid(gridGap16, pl8, pr8) {
                    mainList = this
                }
            }.also {
                mainBox = it
            }
        }
    }

    fun Z2.attachmentFileSelect() {
        fileSelect(fileBrowserOnClick = false, fileSelectedFun = ::onAttachmentSelect) {
            config.dropEnabled = false
            attachmentSelect = this
            addClass(positionRelative, heightFull, overflowHidden)
            scrolledBoxWithLabel(this@FileBundleInput.config.attachments) {
                grid(gridGap16, pl8, pr8) {
                    attachmentList = this
                }
            }.also {
                attachmentBox = it
            }
        }
    }

    fun validate(): Boolean {
        touched = true
        var valid = true

        if (config.showFolderAndTypeSelect && folder == null) {
            folderSelect.state.touched = true
            folderSelect.state.error = true
            valid = false
        }

        if (config.showFolderAndTypeSelect && ::typeSelect.isInitialized) {
            if (typeSelect.valueOrNull == null) {
                valid = false
                errorBorder(typeBox)
            }
        } else {
            errorBorder(typeBox)
            valid = false
        }

        valid = valid and mainValid()
        valid = valid and attachmentsValid()

        return valid
    }

    fun mainValid(): Boolean =
        if (touched && (mainFile.isEmpty() || problems(mainFile.first()).isNotEmpty())) {
            errorBorder(mainBox)
            false
        } else {
            normalBorder(mainBox)
            true
        }

    fun attachmentsValid(): Boolean =
        if (attachments.any { problems(it).isNotEmpty() }) {
            errorBorder(attachmentBox)
            false
        } else {
            normalBorder(attachmentBox)
            true
        }

    fun errorBorder(node: Z2) {
        node.removeClass(borderOutline)
        node.addClass(borderError)
    }

    fun normalBorder(node: Z2) {
        node.removeClass(borderError)
        node.addClass(borderOutline)
    }

    fun refresh() {
        summary.summary()

        if (config.showFolderAndTypeSelect) {
            with(typeList) {
                clear()
                if (folder != null) {
                    radioButtonGroup(type, config.types[folder] ?: emptyList(), itemBuilderFun = config.typeRenderFun) {
                        type = it
                        typeBox.removeClass(borderError)
                        typeBox.addClass(borderOutline)
                    }.also {
                        typeSelect = it
                    }
                } else {
                    div(p16, bodyMedium, onSurfaceVariantText) { + config.selectFolderFirst }
                }
            }
        }

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

        mainValid()
        attachmentsValid()
    }

    fun onMainSelect(selected: List<File>) {
        touched = true
        mainFile = selected
        attachments.removeAll { it.name == mainFile.first().name }
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
        active: Boolean,
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
                div(bodySmall, onSurfaceVariantText, displayFlex, flexDirectionRow) {
                    // TODO remove the lastmodified magic
                    val lastModified = (js("file.lastModified.toString()") as String).toLongOrNull()
                    lastModified?.let { + Instant.fromEpochMilliseconds(lastModified.toLong()).here().localized }

                    problems(file).let {
                        if (it.isNotEmpty()) div(pl16, errorText) { + it.joinToString() }
                    }
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

    fun problems(file: File): MutableList<LocalizedText> {
        val problems = mutableListOf<LocalizedText>()
        if (! extensionOk(file)) problems += config.invalidExtension
        if (! sizeOk(file)) problems += config.sizeOverLimit
        return problems
    }

    fun extensionOk(file: File): Boolean {
        val extensions = config.acceptedExtensions ?: return true
        return file.name.substringAfterLast('.').lowercase() in extensions
    }

    fun sizeOk(file: File): Boolean {
        return file.size.toLong() <= config.sizeLimit
    }

}