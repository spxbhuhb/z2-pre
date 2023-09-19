package hu.simplexion.z2.browser.components.file

import org.w3c.files.File

class FileSelectConfig(
    val multiple : Boolean = true,
    var dropEnabled : Boolean = true,
    val fileBrowserOnClick: Boolean = true,
    val filesSelectedFun : (files: List<File>) -> Unit,
    val renderFun :  (FileSelect.() -> Unit)? = null,
)