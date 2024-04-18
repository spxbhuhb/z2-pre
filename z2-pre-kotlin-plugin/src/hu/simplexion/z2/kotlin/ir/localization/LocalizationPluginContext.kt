/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.localization

import hu.simplexion.z2.kotlin.Z2Options
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class LocalizationPluginContext(
    val options: Z2Options,
    val irContext: IrPluginContext
) {

    companion object {
        const val STATIC_VALUE_ARG_INDEX = 0
        const val STATIC_SUPPORT_ARG_INDEX = 1
        const val STATIC_NAME_ARG_INDEX = 2
        const val ICON_PACKAGE = "hu.simplexion.z2.localization.icon"
        const val LOCALIZATION_PACKAGE = "hu.simplexion.z2.localization"
        const val TEXT_PACKAGE = "hu.simplexion.z2.localization.text"
        const val LOCALIZATION_NAMESPACE = "localizationNamespace"
        const val SCHEMATIC_FQ_NAME = "schematicFqName"
    }

    // ----------------------------------------------------------------------------------
    // Classes, types, functions
    // ----------------------------------------------------------------------------------

    val localizationProvider = "LocalizationProvider".runtimeClass(LOCALIZATION_PACKAGE).owner
    val localizedText = "LocalizedText".runtimeClass(TEXT_PACKAGE)
    val localizedIcon = "LocalizedIcon".runtimeClass(ICON_PACKAGE)

    val nonLocalizedAnnotation = FqName("hu.simplexion.z2.localization.NonLocalized")

    fun String.runtimeClass(pkg: String) =
        checkNotNull(irContext.referenceClass(ClassId(FqName(pkg), Name.identifier(this)))) {
            "Missing ${pkg}.$this class. Maybe the gradle dependency on \"hu.simplexion.z2:z2-core\" is missing."
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