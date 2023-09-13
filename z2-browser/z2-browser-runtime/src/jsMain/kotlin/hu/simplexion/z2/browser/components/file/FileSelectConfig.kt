package hu.simplexion.z2.browser.components.file

import hu.simplexion.z2.browser.html.Z2
import org.w3c.files.File

class FileSelectConfig(
    val multiple : Boolean = true,
    val filesSelectedFun : (files: List<File>) -> Unit,
    val renderFun :  (Z2.() -> Unit)? = null
)