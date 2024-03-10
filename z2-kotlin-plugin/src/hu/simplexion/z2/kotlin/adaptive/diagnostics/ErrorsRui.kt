/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.diagnostics

import hu.simplexion.z2.kotlin.adaptive.ADAPTIVE_STATE_VARIABLE_LIMIT
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.file

object ErrorsAdaptive {
    // These errors are used by the IR transformation

    // IMPORTANT: DO NOT ADD AUTOMATIC ID GENERATION! error ids should not change over time
    val ADAPTIVE_IR_RENDERING_VARIABLE = AdaptiveIrError(1, "Declaration of state variables in rendering part is not allowed.")
    val ADAPTIVE_IR_MISSING_FUNCTION_BODY = AdaptiveIrError(2, "Adaptive annotation is not allowed on functions without block body.")
    val ADAPTIVE_IR_INVALID_RENDERING_STATEMENT = AdaptiveIrError(3, "Statement is not allowed in the rendering part.")

    //    val RIU_IR_RENDERING_NO_LOOP_BODY = AdaptiveIrError(4, "Statement is not allowed in the rendering part.")
    val RIU_IR_RENDERING_NON_ADAPTIVE_CALL = AdaptiveIrError(5, "Calls to non-Adaptive functions is not allowed in the rendering part.")
    val ADAPTIVE_IR_MISSING_EXPRESSION_ARGUMENT = AdaptiveIrError(6, "Missing expression argument. Most probably this is a plugin bug, please open an issue for this on GitHub.")

    //    val ADAPTIVE_IR_RENDERING_INVALID_LOOP_BODY = AdaptiveIrError(7, "This loop body is not allowed in the rendering part.")
    val ADAPTIVE_IR_RENDERING_INVALID_DECLARATION = AdaptiveIrError(8, "This declaration is not allowed in the rendering part.")

    //    val ADAPTIVE_IR_MISSING_ADAPTIVE_CLASS = AdaptiveIrError(9, "Missing Adaptive class.")
    val ADAPTIVE_IR_TOO_MANY_STATE_VARIABLES = AdaptiveIrError(10, "Adaptive can handle maximum $ADAPTIVE_STATE_VARIABLE_LIMIT state variables in one scope.")
    val ADAPTIVE_IR_INVALID_EXTERNAL_CLASS = AdaptiveIrError(11, "Invalid external class: ")
    val ADAPTIVE_IR_INTERNAL_PLUGIN_ERROR = AdaptiveIrError(12, "Internal plugin error: ")

    val ADAPTIVE_IR_STATE_VARIABLE_SHADOW = AdaptiveIrError(13, "Shadowing state variables is not allowed.")

    class AdaptiveIrError(
        val id: Int,
        val message: String,
    ) {
        fun toMessage(): String {
            return "${id.toString().padStart(4, '0')}  $message"
        }

        fun check(armClass: ArmClass, element: IrElement, check: () -> Boolean) {
            if (check()) return
            armClass.compilationError = true
            report(armClass.adaptiveContext, armClass.originalFunction.file.fileEntry, element.startOffset)
        }

        fun report(adaptiveContext: AdaptivePluginContext, declaration: IrFunction, additionalInfo: String = "") {
            report(adaptiveContext, declaration.file.fileEntry, declaration.startOffset, additionalInfo)
        }

//        fun report(adaptiveClassBuilder: AdaptiveClassBuilder, element: IrElement, additionalInfo: String = ""): Nothing? {
//            report(adaptiveClassBuilder.adaptiveContext, adaptiveClassBuilder.armClass.irFunction.file.fileEntry, element.startOffset, additionalInfo)
//            return null
//        }

        fun report(armClass: ArmClass, element: IrElement, additionalInfo: String = ""): Nothing? {
            armClass.compilationError = true
            report(armClass.adaptiveContext, armClass.originalFunction.file.fileEntry, element.startOffset, additionalInfo)
            return null
        }

//        fun report(adaptiveContext: AdaptivePluginContext, irCall: IrCall, additionalInfo: String = "") {
//            report(adaptiveContext, irCall.symbol.owner.file.fileEntry, irCall.startOffset, additionalInfo)
//        }

        fun report(adaptiveContext: AdaptivePluginContext, fileEntry: IrFileEntry, offset: Int, additionalInfo: String = "") {
            adaptiveContext.compilationError = true
            adaptiveContext.report(
                IrMessageLogger.Severity.ERROR,
                toMessage() + " " + additionalInfo,
                IrMessageLogger.Location(
                    fileEntry.name,
                    fileEntry.getLineNumber(offset) + 1,
                    fileEntry.getColumnNumber(offset) + 1
                )
            )
        }
    }
}