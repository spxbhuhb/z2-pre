/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.util

import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.Strings.toNameWithPostfix
import hu.simplexion.z2.kotlin.adaptive.capitalizeFirstChar
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.adaptive.ir.diagnostics.ErrorsAdaptive
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.isAnonymousFunction
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


fun IrFunction.toAdaptiveClassFqName(adaptiveContext: AdaptivePluginContext, anonymous: Boolean): FqName {
    val parent = kotlinFqName.parentOrNull() ?: FqName.ROOT

    return when {

        anonymous -> {
            parent.child(Strings.ADAPTIVE_ANONYMOUS_PREFIX.toNameWithPostfix(startOffset))
        }

        isAnonymousFunction || name.asString() == "<anonymous>" -> {
            val postfix = this.file.name.replace(".kt", "").capitalizeFirstChar() + startOffset.toString()
            parent.child(Strings.ADAPTIVE_ROOT_CLASS_PREFIX.toNameWithPostfix(postfix))
        }

        else -> parent.child(Name.identifier("Adaptive" + name.identifier.capitalizeFirstChar()))
    }
}