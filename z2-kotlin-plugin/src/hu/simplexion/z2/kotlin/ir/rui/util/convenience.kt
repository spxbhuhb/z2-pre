package hu.simplexion.z2.kotlin.ir.rui.util

import hu.simplexion.z2.kotlin.ir.rui.RUI_ANNOTATION
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue

val IrCall.isParameterCall: Boolean
    get() = dispatchReceiver.let { dr ->
        dr is IrGetValue && dr.symbol.owner.annotations.any { it.type.asString() == RUI_ANNOTATION }
    }