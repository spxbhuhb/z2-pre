package hu.simplexion.z2.kotlin

import java.io.File

class Z2Options(
    val resourceOutputDir : File?,
    val adaptiveTrace: Boolean,
    val pluginDebug : Boolean,
    val pluginLogDir : File?
)