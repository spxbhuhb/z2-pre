package hu.simplexion.z2.kotlin.ir.adaptive.util

import hu.simplexion.z2.kotlin.ir.adaptive.ADAPTIVE_ANNOTATION
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrGetValue

/**
 * True when `this` is a call of a `parameter function` (a function that is passed
 * as a parameter to a higher-order function).
 */
val IrCall.isParameterCall: Boolean
    get() = dispatchReceiver.let { dr ->
        dr is IrGetValue && dr.symbol.owner.annotations.any { it.type.asString() == ADAPTIVE_ANNOTATION }
    }