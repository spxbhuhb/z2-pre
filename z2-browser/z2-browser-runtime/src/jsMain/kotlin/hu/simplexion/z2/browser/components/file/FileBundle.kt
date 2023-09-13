package hu.simplexion.z2.browser.components.file

import org.w3c.files.File

class FileBundle<T>(
    val type : T,
    val name : String,
    val files : List<File>
)
