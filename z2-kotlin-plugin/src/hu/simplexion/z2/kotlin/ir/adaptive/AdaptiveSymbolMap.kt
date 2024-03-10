/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive

import hu.simplexion.z2.kotlin.ir.adaptive.diagnostics.ErrorsAdaptive.ADAPTIVE_IR_INVALID_EXTERNAL_CLASS
import hu.simplexion.z2.kotlin.ir.adaptive.util.AdaptiveCompilationException
import hu.simplexion.z2.kotlin.ir.adaptive.util.isSynthetic
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.name.FqName

class AdaptiveSymbolMap(
    val adaptiveContext: AdaptivePluginContext
) {

    private val classSymbolMaps = mutableMapOf<FqName, AdaptiveClassSymbols>()

    fun getSymbolMap(fqName: FqName): AdaptiveClassSymbols =
        classSymbolMaps[fqName] ?: loadClass(fqName)

    private fun loadClass(fqName: FqName): AdaptiveClassSymbols {
        val irClass =
            adaptiveContext.airClasses[fqName]?.irClass
                ?: adaptiveContext.classSymbol(fqName).owner

        return AdaptiveClassSymbols(irClass).also {
            classSymbolMaps[fqName] = it
        }
    }

}

/**
 * Helps lookup of state variables and functions in Adaptive classes.
 *
 * @property  stateVariables  List of external state variables. The list contains the state variables in order
 *                            they are declared in the original function. So, parameter position may be used
 *                            to index it.
 *
 */
class AdaptiveClassSymbols(
    val irClass: IrClass,
    var valid: Boolean = true
) {
    private val stateVariables = mutableListOf<AdaptiveStateVariableSymbol>()

    private val invalidateFunctions: List<IrSimpleFunctionSymbol>

    lateinit var externalPatch: IrProperty
    lateinit var externalPatchGetter: IrSimpleFunction

    lateinit var create: IrSimpleFunction
    lateinit var mount: IrSimpleFunction
    lateinit var patch: IrSimpleFunction
    lateinit var dispose: IrSimpleFunction
    lateinit var unmount: IrSimpleFunction

    val defaultType
        get() = irClass.defaultType

    val primaryConstructor
        get() = irClass.primaryConstructor ?: throw AdaptiveCompilationException(ADAPTIVE_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing primary constructor")

    init {

        val indices = primaryConstructor.valueParameters.map { it.name.identifier }

        val invalidate = mutableListOf<IrSimpleFunction>()

        irClass.declarations.forEach {
            when {
                it is IrSimpleFunction -> function(invalidate, it)
                it is IrProperty && !it.name.isSynthetic() -> property(indices, it)
            }
        }

        invalidateFunctions = invalidate.sortedBy { it.name }.map { it.symbol }

        stateVariables.sortBy { it.index }

        if (!::externalPatch.isInitialized) {
            throw AdaptiveCompilationException(ADAPTIVE_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing $ADAPTIVE_EXTERNAL_PATCH property")
        }

        if (
            !::create.isInitialized ||
            !::mount.isInitialized ||
            !::patch.isInitialized ||
            !::mount.isInitialized ||
            !::dispose.isInitialized
        ) {
            throw AdaptiveCompilationException(ADAPTIVE_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing default Adaptive method(s)")
        }
    }

    private fun function(invalidate: MutableList<IrSimpleFunction>, it: IrSimpleFunction) {
        if (it.name.identifier.startsWith(ADAPTIVE_INVALIDATE)) {
            invalidate += it
            return
        }

        when (it.name.identifier) {
            ADAPTIVE_CREATE -> create = it
            ADAPTIVE_MOUNT -> mount = it
            ADAPTIVE_PATCH -> patch = it
            ADAPTIVE_UNMOUNT -> unmount = it
            ADAPTIVE_DISPOSE -> dispose = it
        }
    }

    private fun property(indices: List<String>, it: IrProperty) {
        val name = it.name.identifier
        val index = indices.indexOf(name) - Indices.ADAPTIVE_FRAGMENT_ARGUMENT_COUNT

        if (name == ADAPTIVE_EXTERNAL_PATCH) {
            externalPatch = it
            externalPatchGetter = it.getter ?: throw AdaptiveCompilationException(ADAPTIVE_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "missing $ADAPTIVE_EXTERNAL_PATCH getter")
            return
        }

        if (index >= 0) {
            stateVariables += AdaptiveStateVariableSymbol(index, it)
            return
        }
    }

    fun setterFor(index: Int): IrSimpleFunctionSymbol =
        getStateVariable(index).property.setter?.symbol
            ?: throw AdaptiveCompilationException(ADAPTIVE_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "no setter for $index")

    fun getStateVariable(index: Int): AdaptiveStateVariableSymbol =
        if (stateVariables.size > index) {
            stateVariables[index]
        } else {
            throw AdaptiveCompilationException(ADAPTIVE_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "no state variable for $index")
        }

    fun getInvalidate(index: Int): IrFunctionSymbol =
        if (invalidateFunctions.size > index) {
            invalidateFunctions[index]
        } else {
            throw AdaptiveCompilationException(ADAPTIVE_IR_INVALID_EXTERNAL_CLASS, additionalInfo = "no state variable for $index")
        }

}

class AdaptiveStateVariableSymbol(
    val index: Int,
    val property: IrProperty
)
