/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.klass

import hu.simplexion.z2.schematic.kotlin.ir.COMPANION_OBJECT_NAME
import hu.simplexion.z2.schematic.kotlin.ir.FIELD_CONSTRUCTOR_NAME_INDEX
import hu.simplexion.z2.schematic.kotlin.ir.FIELD_CONSTRUCTOR_NULLABLE_INDEX
import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import hu.simplexion.z2.schematic.kotlin.ir.util.IrBuilder
import hu.simplexion.z2.schematic.kotlin.ir.util.SchematicFunctionType
import org.jetbrains.kotlin.backend.jvm.ir.receiverAndArgs
import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.backend.js.utils.typeArguments
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

/**
 * Analyze the FDF call and the possible DEF calls.
 * Build the SchemaField constructor call.
 */
class SchematicFieldVisitor(
    override val pluginContext: SchematicPluginContext,
) : IrElementVisitorVoid, IrBuilder {

    lateinit var property: IrProperty
    lateinit var type: IrType
    lateinit var schemaField: IrVarargElement

    val callChain = mutableListOf<IrCall>()

    var nullable = false

    override fun visitProperty(declaration: IrProperty) {

        property = declaration

        val backingField = checkNotNull(declaration.backingField) { "missing backing field" }

        // this is the call to provideDelegate
        // it's first argument is the call to the type function (`int`, `string` etc.)

        val provideDelegateCall = checkNotNull(backingField.initializer?.expression) { "missing backing field expression" }
        check(provideDelegateCall is IrCall) { "backing field expression is not a call ${property.parent.kotlinFqName}.${property.name}" }

        destructCallChain(provideDelegateCall)
        processCallChain()

        buildSchemaField()
    }

    fun destructCallChain(provideDelegateCall: IrCall) {
        // this is one of:
        //   - a call to the FDF function `int`, `string`, etc.
        //   - a call to an DTF function such as `nullable`

        var nextCall: IrExpression? = provideDelegateCall.receiverAndArgs().first()
        check(nextCall is IrCall) { "delegate provider argument is not a call ${property.parent.kotlinFqName}.${property.name}" }

        var currentCall: IrCall = nextCall
        var callType = pluginContext.funCache[currentCall]

        while (callType == SchematicFunctionType.DefinitionTransform) {
            callChain += currentCall

            nextCall = currentCall.extensionReceiver
            check(nextCall != null && nextCall is IrCall) { "delegation call chain contains a non-call receiver" }

            currentCall = nextCall
            callType = pluginContext.funCache[currentCall]
        }

        check(callType == SchematicFunctionType.FieldDefinition) { "delegation call chain does not end with an FDF call" }
        callChain += currentCall
    }

    fun processCallChain() {
        // this is a bit hackish, but it will work for now
        for (call in callChain) {
            if (call.symbol.owner.name.identifier == "nullable") {
                nullable = true
            }
        }
    }

    fun buildSchemaField() {
        val fdfCall = callChain.last()
        val valueArguments = fdfCall.valueArguments
        val fieldClass = pluginContext.funCache.getFieldClass(fdfCall.symbol)

        val constructor = fieldClass.constructor.owner
        val constructorParameters = constructor.valueParameters

        val companions = fdfCall.typeArguments
            .filterNotNull()
            .filter { it.isSubtypeOfClass(pluginContext.schematicClass) }
            .map { it.getCompanion() }

        schemaField =
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                fieldClass.type,
                constructor.symbol,
                fdfCall.typeArgumentsCount,
                0,
                constructorParameters.size
            ).also { constructorCall ->

                for (index in fdfCall.typeArguments.indices) {
                    constructorCall.putTypeArgument(index, fdfCall.typeArguments[index])
                }

                constructorCall.putValueArgument(FIELD_CONSTRUCTOR_NAME_INDEX, irConst(property.name.identifier))
                constructorCall.putValueArgument(FIELD_CONSTRUCTOR_NULLABLE_INDEX, irConst(nullable))

                var index = FIELD_CONSTRUCTOR_NULLABLE_INDEX + 1

                // TODO add a parameter name and type match check to SchemaField builder, should cache it probably
                for (valueArgument in valueArguments) {
                    if (valueArgument != null) {
                        constructorCall.putValueArgument(index ++, valueArgument)
                    } else {
                        constructorCall.putValueArgument(index, irNull(constructorParameters[index].type))
                        index++
                    }
                }

                // This adds the companions for nested schematics.
                // I feel that this is somewhat hackish, will work, probably.
                // When the field class does not want the companions they will be simply skipped.
                for (companion in companions) {
                    if (index < constructorParameters.size) {
                        constructorCall.putValueArgument(index ++, irGetObject(companion.symbol))
                    }
                }
            }
    }

    private fun IrType.getCompanion(): IrClass {
        val companion = getClass()?.companionObject()
        if (companion != null) return companion

        val typeFqName = checkNotNull(classFqName) { "cannot load companion for ${this.asString()}" }
        val classId = ClassId(typeFqName.parent(), Name.identifier(typeFqName.shortName().identifier + "\$Companion")) //.createNestedClassId(Name.identifier(COMPANION_OBJECT_NAME))

        return checkNotNull(pluginContext.irContext.referenceClass(classId)) { "cannot load companion for ${classId.asString()}" }.owner
    }
}

