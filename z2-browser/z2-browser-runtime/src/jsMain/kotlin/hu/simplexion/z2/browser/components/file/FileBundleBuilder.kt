package hu.simplexion.z2.browser.components.file

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.radiobutton.radioButtonGroup
import hu.simplexion.z2.browser.material.snackbar.snackbar
import hu.simplexion.z2.browser.material.stateLayer
import hu.simplexion.z2.browser.material.textfield.TextField
import hu.simplexion.z2.browser.material.textfield.textField
import hu.simplexion.z2.commons.i18n.locales.localized
import hu.simplexion.z2.commons.util.formatByteLength
import hu.simplexion.z2.commons.util.here
import kotlinx.datetime.Instant
import org.w3c.files.File

class FileBundleBuilder<T>(
    parent: Z2,
    val config: FileBundleBuilderConfiguration<T>
) : Z2(parent) {

    var name
        get() = nameField.value
        set(value) {
            nameField.value = value
        }

    var autoName: Boolean = true
    var type: T = config.types.first()
    val files = mutableListOf<File>()

    lateinit var fileList: Z2
    lateinit var summary: Z2
    lateinit var fileSelect: FileSelect
    lateinit var nameField: TextField

    init {
        addClass(overflowHidden, displayGrid, p16, positionRelative, boxSizingBorderBox)
        gridTemplateColumns = "max-content minmax(600px,1fr)"
        gridTemplateRows = "minmax(200px, 1fr) min-content"
        columnGap = 16.px

        div(overflowYAuto, heightFull, boxSizingBorderBox, pr16, borderOutline, borderRadius4) {
            div(labelMedium, pl12, pt8, pb8) { + config.typeLabel }
            radioButtonGroup(type, config.types, itemBuilderFun = config.typeRenderFun) { type = it }
        }

        grid(positionRelative) {
            gridTemplateColumns = "1fr"
            gridTemplateRows = "min-content min-content 1fr"

            nameField = textField("", FieldStyle.Outlined, config.bundleNameLabel) { onNameChange(it) }
                .also { it.config.supportEnabled = false }

            div(displayFlex, justifyContentFlexEnd, pt12) {
                textButton(config.dropFilesHereLabel, false) { fileSelect.openFileBrowser() }
            }

            fileSelect(fileBrowserOnClick = false, fileSelectedFun = ::onFileSelect) {
                fileSelect = this
                addClass(heightFull, overflowYAuto, positionRelative, borderOutline, borderRadius4)
                grid(gridGap16, pl8, pr8) {
                    fileList = this
                    gridTemplateRows = "min-content 1fr"
                }
            }
        }

        div { } // placeholder under type
        div(pt8, justifySelfEnd) { summary = this }

        refresh()
    }

    fun onNameChange(value: String) {
        name = value
        autoName = false
        config.nameValidationFun?.invoke(nameField) ?: false
    }

    fun onFileSelect(selected: List<File>) {
        val filtered = selected.filter { ! files.any { f -> it.name == f.name } }
        if (filtered.size != selected.size) snackbar(config.fileAlreadyAddedLabel)
        files += filtered

        if (autoName && name.isBlank() && files.isNotEmpty()) {
            autoName = true
            name = files.first().name.substringBeforeLast('.')
            nameField.value = name
        }

        refresh()
    }

    fun refresh() {
        summary.summary()
        fileList.fileList(files)
    }

    fun Z2.summary() {
        clear()

        val total = files.sumOf { it.size.toLong() }

        grid(bodySmall, onSurfaceVariantText, pl16, pr16) {
            gridTemplateColumns = "1fr max-content"
            div {
                if (files.isNotEmpty()) {
                    + "${files.size} "
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

    fun Z2.fileList(files: List<File>) {
        clear()

        grid(overflowYAuto, gridGap8, pt8) {
            for (file in files) {
                fileListEntry(file)
            }
        }
    }

    fun Z2.fileListEntry(file: File) {
        grid(gridGap8, positionRelative, pt8, pb8, pl12, pr12) {
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
                    files.removeAll { it.name == file.name }
                    fileSelect.inputElement.value = ""
                    if (files.isEmpty() && autoName) name = ""
                    refresh()
                }
            }
        }
    }
}