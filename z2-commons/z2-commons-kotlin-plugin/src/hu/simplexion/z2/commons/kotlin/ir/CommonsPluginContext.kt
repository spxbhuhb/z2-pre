/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.kotlin.ir

import hu.simplexion.z2.commons.kotlin.ir.plugin.CommonsOptions
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class CommonsPluginContext(
    val options : CommonsOptions,
    val irContext: IrPluginContext
) {

    companion object {
        const val PROPERTY_NAME_INDEX = 2
        const val ICON_PACKAGE = "hu.simplexion.z2.commons.localization.icon"
        const val LOCALIZATION_PACKAGE = "hu.simplexion.z2.commons.localization"
        const val TEXT_PACKAGE = "hu.simplexion.z2.commons.localization.text"
        const val LOCALIZATION_NAMESPACE = "localizationNamespace"
    }

    // ----------------------------------------------------------------------------------
    // Classes, types, functions
    // ----------------------------------------------------------------------------------

    val localizationProvider = "LocalizationProvider".runtimeClass(LOCALIZATION_PACKAGE).owner
    val localizedText = "LocalizedText".runtimeClass(TEXT_PACKAGE)
    val localizedIcon = "LocalizedIcon".runtimeClass(ICON_PACKAGE)


    fun String.runtimeClass(pkg: String) =
        checkNotNull(irContext.referenceClass(ClassId(FqName(pkg), Name.identifier(this)))) {
            "Missing ${pkg}.$this class. Maybe the gradle dependency on \"hu.simplexion.z2:z2-commons-runtime\" is missing."
        }

    // ----------------------------------------------------------------------------------
    // Collected information
    // ----------------------------------------------------------------------------------

    val resources = mutableSetOf<String>()

    // ----------------------------------------------------------------------------------
    // Plugin debug
    // ----------------------------------------------------------------------------------

    @Suppress("UNUSED_PARAMETER")
    fun debug(label: String, message: () -> Any?) {
//        val paddedLabel = "[$label]".padEnd(30)
//        Files.write(Paths.get("/Users/tiz/Desktop/plugin.txt"), "$paddedLabel  ${message()}\n".encodeToByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    }
}