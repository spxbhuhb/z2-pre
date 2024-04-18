package hu.simplexion.z2.kotlin.schematic.ir.util

import hu.simplexion.z2.kotlin.schematic.ir.SchematicPluginContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType

/**
 * A cache that stores the type of each function the plugin encounters.
 * This hopefully speeds up the compilation as we don't have to check
 * the annotations on each function again and again.
 */
class SchematicFunctionCache(
    val pluginContext: SchematicPluginContext
) {

    val types = mutableMapOf<IrSymbol, SchematicFunctionType>()

    class FieldClassEntry(
        val type: IrType,
        val constructor: IrConstructorSymbol
    )

    operator fun get(call: IrCall): SchematicFunctionType = get(call.symbol)

    operator fun get(symbol: IrSimpleFunctionSymbol): SchematicFunctionType {
        return types.getOrPut(symbol) { add(symbol) }
    }

    fun add(symbol: IrSimpleFunctionSymbol): SchematicFunctionType {
        val function = symbol.owner
        val annotations = function.annotations
        if (annotations.isEmpty()) return SchematicFunctionType.Other

        for (annotation in function.annotations) {
            when (annotation.symbol) {
                pluginContext.safAnnotationConstructor -> return SchematicFunctionType.SchematicAccess
            }
        }

        return SchematicFunctionType.Other
    }

}