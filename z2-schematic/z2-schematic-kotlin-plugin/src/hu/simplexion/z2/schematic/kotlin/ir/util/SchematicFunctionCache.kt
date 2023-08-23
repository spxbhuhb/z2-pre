package hu.simplexion.z2.schematic.kotlin.ir.util

import hu.simplexion.z2.schematic.kotlin.ir.FDF_ANNOTATION_FIELD_CLASS_INDEX
import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.util.primaryConstructor

/**
 * A cache that stores the type of each function the plugin encounters.
 * This hopefully speeds up the compilation as we don't have to check
 * the annotations on each function again and again.
 */
class SchematicFunctionCache(
    val pluginContext: SchematicPluginContext
) {

    val types = mutableMapOf<IrSymbol, SchematicFunctionType>()
    val fieldClasses = mutableMapOf<IrSymbol, FieldClassEntry>()

    class FieldClassEntry(
        val type : IrType,
        val constructor : IrConstructorSymbol
    )

    operator fun get(call: IrCall): SchematicFunctionType = get(call.symbol)

    operator fun get(symbol: IrSimpleFunctionSymbol): SchematicFunctionType {
        return types.getOrPut(symbol) { add(symbol) }
    }

    fun getFieldClass(symbol : IrSymbol) : FieldClassEntry {
        return checkNotNull(fieldClasses[symbol]) { "missing field class for $symbol"}
    }

    fun add(symbol: IrSimpleFunctionSymbol): SchematicFunctionType {
        val function = symbol.owner
        val annotations = function.annotations
        if (annotations.isEmpty()) return SchematicFunctionType.Other

        for (annotation in function.annotations) {
            when (annotation.symbol) {
                pluginContext.fdfAnnotationConstructor -> return add(symbol, annotation)
                pluginContext.dtfAnnotationConstructor -> return SchematicFunctionType.DefinitionTransform
                pluginContext.safAnnotationConstructor -> return SchematicFunctionType.SchematicAccess
            }
        }

        return SchematicFunctionType.Other
    }

    fun add(symbol: IrSimpleFunctionSymbol, annotation: IrConstructorCall): SchematicFunctionType {
        val fieldClassExpression = annotation.getValueArgument(FDF_ANNOTATION_FIELD_CLASS_INDEX)
        check(fieldClassExpression is IrClassReference) { "FDF annotation parameter is not a class reference" }

        val classType = fieldClassExpression.classType
        val constructor = checkNotNull(classType.getClass()?.primaryConstructor?.symbol) { "missing field class constructor for $classType" }

        fieldClasses[symbol] = FieldClassEntry(classType, constructor)

        return SchematicFunctionType.FieldDefinition
    }

}