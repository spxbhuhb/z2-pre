/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.util

import hu.simplexion.z2.kotlin.adaptive.capitalizeFirstChar
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.adaptive.ir.diagnostics.ErrorsAdaptive
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.parentOrNull

fun Name.isSynthetic() = identifier.startsWith('$') || identifier.endsWith('$')

class AdaptiveCompilationException(
    val error: ErrorsAdaptive.AdaptiveIrError,
    var armClass: ArmClass? = null,
    var irElement: IrElement? = null,
    val additionalInfo: String = ""
) : Exception() {

    var reported = false

    init {
        report()
    }

    fun report() {
        if (reported) return
        armClass?.let { c ->
            irElement?.let { e ->
                error.report(c, e, additionalInfo)
                reported = true
            }
        }
    }
}


fun IrFunction.adaptiveClassFqName(): FqName {
    val parent = kotlinFqName.parentOrNull() ?: FqName.ROOT
    return parent.child(Name.identifier("Adaptive" + name.identifier.capitalizeFirstChar()))
}