/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.css

import hu.simplexion.z2.kotlin.ir.Z2Options
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class CssPluginContext(
    val options : Z2Options,
    val irContext: IrPluginContext
) {

    companion object {
        const val COMMON_PACKAGE = "hu.simplexion.z2.commons.browser"
    }

    val cssClass = "CssClass".runtimeClass(COMMON_PACKAGE)

    fun String.runtimeClass(pkg: String) =
        checkNotNull(irContext.referenceClass(ClassId(FqName(pkg), Name.identifier(this)))) {
            "Missing ${pkg}.$this class. Maybe the gradle dependency on \"hu.simplexion.z2:z2-commons\" is missing."
        }

}