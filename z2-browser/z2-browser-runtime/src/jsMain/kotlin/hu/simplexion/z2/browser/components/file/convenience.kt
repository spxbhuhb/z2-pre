package hu.simplexion.z2.browser.components.file

import hu.simplexion.z2.browser.html.Z2
import org.w3c.files.File

fun Z2.fileSelect(fileSelectedFun: (file: List<File>) -> Unit) =
    FileSelect(this, FileSelectConfig(filesSelectedFun = fileSelectedFun))

fun Z2.fileSelect(
    multiple: Boolean = true,
    dropEnabled: Boolean = true,
    fileBrowserOnClick: Boolean = true,
    fileSelectedFun: (file: List<File>) -> Unit,
    rendererFun: (FileSelect.() -> Unit)? = null
) =
    FileSelect(
        this,
        FileSelectConfig(
            multiple,
            dropEnabled,
            fileBrowserOnClick,
            fileSelectedFun,
            rendererFun
        )
    )
