/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.klass

import hu.simplexion.z2.schematic.kotlin.ir.*
import hu.simplexion.z2.schematic.kotlin.ir.companion.CompanionTransform
import hu.simplexion.z2.schematic.kotlin.ir.util.IrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getPropertyGetter

class SchematicClassTransform(
    override val pluginContext: SchematicPluginContext
) : IrElementTransformerVoidWithContext(), IrBuilder {

    lateinit var transformedClass: IrClass
    lateinit var schematicValuesGetter: IrFunctionSymbol
    lateinit var schematicChange: IrFunctionSymbol

    lateinit var companionTransform: CompanionTransform

    // index of the field in `Schema.fields`
    var fieldIndex = 0

    // used by companion transform to make schema constructor, setFieldValue, getFieldValue
    val fieldVisitors = mutableListOf<SchematicFieldVisitor>()

    fun initialize(declaration: IrClass) {
        transformedClass = declaration
        schematicChange = findSchematicChange()
        schematicValuesGetter = checkNotNull(declaration.getPropertyGetter(SCHEMATIC_VALUES_PROPERTY)) { "missing $SCHEMATIC_VALUES_PROPERTY getter " }

        companionTransform = CompanionTransform(pluginContext, this)
        companionTransform.addOrGetCompanionClass()

        addInitializer()
    }

    /**
     * Transforms the properties and builds the schemas. Has to run this after all
     * companions are added.
     */
    fun transformFields() {
        visitClassNew(transformedClass)
        companionTransform.finish()
    }

    private fun addInitializer() {
        val initializer = irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        )

        initializer.parent = transformedClass
        initializer.body = DeclarationIrBuilder(irContext, initializer.symbol).irBlockBody {
            +irCall(
                pluginContext.schemaInitWithDefaults,
                dispatchReceiver = irCall(
                    companionTransform.companionSchematicSchemaGetter,
                    dispatchReceiver = irGetObject(companionTransform.companionClass.symbol)
                ),
                args = arrayOf(irGet(transformedClass.thisReceiver!!))
            )
        }

        transformedClass.declarations += initializer
    }

    private fun findSchematicChange(): IrFunctionSymbol =
        checkNotNull(
            transformedClass.functions.firstOrNull {
                it.name.identifier == SCHEMATIC_CHANGE &&
                    it.valueParameters.size == 2 &&
                    it.valueParameters[0].type == irBuiltIns.intType &&
                    it.valueParameters[1].type == irBuiltIns.anyNType
            }?.symbol
        ) { "missing schematicChange function" }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {

        val name = declaration.name.identifier

        if (name == SCHEMATIC_SCHEMA_PROPERTY) {
            return declaration.accept(SchematicSchemaPropertyTransform(pluginContext, this), null) as IrStatement
        }

        if (name == SCHEMATIC_COMPANION_PROPERTY) {
            return declaration.accept(SchematicCompanionPropertyTransform(pluginContext, this), null) as IrStatement
        }

        if (!declaration.isDelegated) {
            return declaration
        }

        val fieldVisitor = SchematicFieldVisitor(pluginContext)

        declaration.accept(fieldVisitor, null)
        fieldVisitors += fieldVisitor

        return declaration.accept(SchematicPropertyTransform(pluginContext, this, fieldVisitor, fieldIndex++), null) as IrStatement
    }

}
