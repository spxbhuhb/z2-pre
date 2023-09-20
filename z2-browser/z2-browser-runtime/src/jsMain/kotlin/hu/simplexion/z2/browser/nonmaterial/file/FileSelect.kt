package hu.simplexion.z2.browser.nonmaterial.file

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.cursorPointer
import hu.simplexion.z2.browser.css.displayNone
import hu.simplexion.z2.browser.css.heightMinContent
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.surfaceContainerHigh
import org.w3c.dom.DragEvent
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.get
import org.w3c.files.File
import org.w3c.files.get

class FileSelect(
    parent: Z2,
    val config: FileSelectConfig
) : Z2(parent) {

    val input = input(displayNone) {
        htmlElement as HTMLInputElement
        htmlElement.type = "file"
        htmlElement.multiple = config.multiple
        onClick {
            htmlElement.value = ""
            it.stopPropagation()  // so the parent won't receive the `input.inputElement.click()` event
        }
        onChange { inputChange() }
    }

    val inputElement: HTMLInputElement
        get() = input.htmlElement as HTMLInputElement

    init {
        if (config.renderFun == null) {
            addClass(cursorPointer, heightMinContent)
            surfaceContainerHigh {
                + browserStrings.dropAttachmentHere
            }
        } else {
            config.renderFun.invoke(this)
        }

        if (config.fileBrowserOnClick) {
            onClick { openFileBrowser() }
        }

        onDrop { if (config.dropEnabled) drop(it) }

        onDragover { it.preventDefault() }
    }

    fun openFileBrowser() {
        inputElement.click()
    }

    fun inputChange() {
        inputElement.files?.let { files ->
            val result = mutableListOf<File>()
            for (index in 0..files.length) {
                files[index]?.let { result += it }
            }
            if (result.isNotEmpty()) config.filesSelectedFun(result)
        }
    }

    fun drop(event: DragEvent) {
        event.stopPropagation()
        event.preventDefault()

        val dataTransfer = event.dataTransfer ?: return
        val files = mutableListOf<File>()

        for (index in 0..dataTransfer.items.length) {
            dataTransfer.items[index]?.let { item ->
                if (item.kind == "file") item.getAsFile()?.let { files += it }
            }
            if (!config.multiple) break
        }

        if (files.isNotEmpty()) config.filesSelectedFun(files)
    }

}